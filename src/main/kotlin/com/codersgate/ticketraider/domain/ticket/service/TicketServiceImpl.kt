package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.event.model.seat.Bookable
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.SeatInfo
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.domain.ticket.entity.TicketStatus
import com.codersgate.ticketraider.domain.ticket.repository.TicketRepository
import com.codersgate.ticketraider.global.error.exception.InvalidCredentialException
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import org.hibernate.Hibernate
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlin.math.log

@Service
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository,
    private val redissonClient: RedissonClient,
    private val cacheManager: CacheManager,
) : TicketService {
    companion object {
        val logger = LoggerFactory.getLogger(TicketServiceImpl::class.java)
    }

    override fun createTicket(userPrincipal: UserPrincipal, request: CreateTicketRequest) {

        val lockList = mutableListOf<RLock>()

        // 락 생성
        for (i in 0 until request.seatList.size) {
            val key = generateKey(
                request.eventId,
                request.date,
                request.seatList[i].ticketGrade,
                request.seatList[i].seatNumber
            )
            val lock = redissonClient.getLock(key)
            lockList.add(lock)

            lock.tryLock(10, 10, TimeUnit.SECONDS) //획득시도 시간, 락 점유 시간
        }

        // 캐시에 존재하는지 확인
//        for (i in 0 until request.seatList.size) {
//            if (chkTicketCache(
//                    request.eventId,
//                    request.date,
//                    request.seatList[i].ticketGrade,
//                    request.seatList[i].seatNumber
//                )
//                ) {
//                logger.info("${request.seatList[i].seatNumber} 번 좌석은 이미 예약되어 있습니다.")
//                request.seatList.removeAt(i)
//                lockList[i].unlock()
//            }
//        }

        val event = eventRepository.findByIdOrNull(request.eventId)
            ?: throw ModelNotFoundException("event", request.eventId)

        Hibernate.initialize(event.availableSeats)   // 컬렉션을 명시적으로 초기화 ( LAZY 모드 )

        // 예약 날짜 체크
        if (request.date < event.startDate || request.date > event.endDate || request.date < LocalDate.now())
            throw IllegalArgumentException("예매일(${request.date})이 올바르지 않습니다.")

        // 좌석 예약 가능 상태 확인
        val availableSeat = event.availableSeats.find {
            it.date == request.date && it.bookable == Bookable.OPEN
        } ?: throw IllegalArgumentException("예매일(${request.date}) 의 예약이 불가능한 상태 입니다.")

        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("member", userPrincipal.id)

        val deleteIndex = mutableSetOf<Int>()
        for (i in 0 until request.seatList.size) {
            // 데이터베이스 체크
            if( chkTicketRepository(
                request.eventId,
                request.date,
                request.seatList[i].ticketGrade,
                request.seatList[i].seatNumber
            )){
                logger.info("${request.seatList[i].seatNumber} 번 좌석은 선택할 수 없습니다. ( 이미 예약된 좌석 in DB )")
                deleteIndex.add(i)
                continue
            }

            // 좌석 번호 체크
            val seatLimit = when (request.seatList[i].ticketGrade) {
                TicketGrade.R -> event.availableSeats[0].maxSeatR
                TicketGrade.S -> event.availableSeats[0].maxSeatS
                TicketGrade.A -> event.availableSeats[0].maxSeatA
            }
            if (request.seatList[i].seatNumber !in 1..<seatLimit){
                logger.info("${request.seatList[i].seatNumber} 번 좌석은 선택할 수 없습니다. (좌석 번호 초과)")
                deleteIndex.add(i)
                continue
            }

            deleteIndex.map{
                request.seatList.removeAt(it)
            }

            // 티켓 생성
            ticketRepository.save(
                Ticket(
                    date = request.date,
                    grade = request.seatList[i].ticketGrade,
                    seatNo = request.seatList[i].seatNumber,
                    event = event,
                    member = member,
                    price = when (request.seatList[i].ticketGrade) {
                        TicketGrade.R -> event.price!!.seatRPrice
                        TicketGrade.S -> event.price!!.seatSPrice
                        TicketGrade.A -> event.price!!.seatAPrice
                    },
                    place = event.place.name
                )

            ).also {

                // 캐시에 추가
//                putTicketCache(
//                    request.eventId,
//                    request.date,
//                    request.seatList[i].ticketGrade,
//                    request.seatList[i].seatNumber,
//                    TicketResponse.from(it)
//                )

                // 락 해제
                lockList[i].unlock()

                // 좌석 예약 수
                availableSeat.increaseSeat(request.seatList[i].ticketGrade)
                if (availableSeat.isFull())
                    availableSeat.close()


                eventRepository.save(event)
            }//also
        }//for
    }

    fun chkTicketCache(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): Boolean {
        logger.info("캐시의 티켓 확인 시작")
        val cache = cacheManager.getCache("tickets")
        logger.info("Cache : $cache")
        val key = "${eventId}_${date}_${grade}_${seatNo}"
        val ticket = cache?.get(key) // ticket : TicketResponse 상태

        if (ticket != null) {    // 캐시에 일치하는 키 있을 때
            logger.info("Cache hit for ticket: $key")
            logger.info("Ticket in Cache : $ticket")
            logger.info("이미 예매된 티켓입니다. ( in cache ) ")
            return true
        } else {   // 캐시에 일치하는 키 없을 때
            logger.info("Cache miss for ticket: $key")
            return false
        }
    }

    fun chkTicketRepository(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): Boolean {
        logger.info("레포지토리의 티켓 확인 시작")
        ticketRepository.chkTicket(eventId, date, grade, seatNo)
            ?.let {
                putTicketCache(
                    eventId,
                    date,
                    grade,
                    seatNo,
                    TicketResponse.from(it)
                )    // 예매된 티켓인데 캐시 저장 안되어있을 경우 캐시에 다시 등록
                logger.info("이미 레포지토리에 존재하는 티켓입니다.")
                return true
            }
            ?: return false
    }


    fun putTicketCache(
        eventId: Long,
        date: LocalDate,
        grade: TicketGrade,
        seatNo: Int,
        ticketResponse: TicketResponse
    ) {
        val cache = cacheManager.getCache("tickets")
        val key = "${eventId}_${date}_${grade}_${seatNo}"
        cache?.put(key, ticketResponse)
        logger.info("Put Cache : $cache::$key")
    }

    override fun getAllTicketList(pageable: Pageable, memberId: Long?, eventId: Long?): Page<TicketResponse> {
        return ticketRepository.getAllTicketList(pageable, memberId, eventId).map { TicketResponse.from(it) }
    }

    override fun getTicketById(ticketId: Long): TicketResponse {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        return TicketResponse.from(ticket)
    }

    override fun getTicketListByUserId(userPrincipal: UserPrincipal, pageable: Pageable): Page<TicketResponse> {
        return ticketRepository.getListByUserId(pageable, userPrincipal.id).map { TicketResponse.from(it) }
    }

    // 없어도 될듯
    override fun updateTicketStatus(ticketId: Long, ticketStatus: TicketStatus) {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)

        ticket.switchTicketStatus(ticketStatus)

        ticketRepository.save(ticket)
    }

    override fun chkExpiredTickets() {
        ticketRepository.findAll().map {
            if (it.date < LocalDate.now())
                it.ticketStatus = TicketStatus.EXPIRED
            ticketRepository.save(it)
        }
    }

    override fun makePayment(
        userPrincipal: UserPrincipal,
        ticketIdList: MutableList<Long>
    ): MutableList<TicketResponse> {

        val paidTicketList: MutableList<TicketResponse> = mutableListOf()

        ticketRepository.findAllByMemberId(userPrincipal.id).map {
            if (it.id in ticketIdList && it.ticketStatus == TicketStatus.UNPAID) {

                // TODO() 결제로직

                it.ticketStatus = TicketStatus.PAID
                ticketRepository.save(it)
                paidTicketList.add(TicketResponse.from(it))
            }
        }

        return paidTicketList
    }

    override fun cancelTicket(ticketId: Long, userPrincipal: UserPrincipal) {
        ticketRepository.findByIdOrNull(ticketId)
            ?.let {
                if (it.member.id == userPrincipal.id) {
                    it.event.availableSeats.map{seat ->
                        if(seat.date == it.date)
                            seat.decreaseSeat(it.grade)
                    }
                }
                else
                    throw InvalidCredentialException("")
            }
    }

    override fun deleteTicket(ticketId: Long) {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        ticketRepository.delete(ticket)
    }
}

private fun generateKey(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): String {
    val key = "ID : ${eventId}, $date : ${grade}-${seatNo}"
    return key
}