package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.event.model.seat.Bookable
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.repository.seat.AvailableSeatRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.SeatInfo
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.domain.ticket.entity.TicketStatus
import com.codersgate.ticketraider.domain.ticket.repository.TicketRepository
import com.codersgate.ticketraider.global.common.aop.redis.lock.PubSubLock
import com.codersgate.ticketraider.global.error.exception.InvalidCredentialException
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import com.codersgate.ticketraider.global.error.exception.TicketReservationFailedException
import com.codersgate.ticketraider.global.infra.redis.cache.CacheTarget
import com.codersgate.ticketraider.global.infra.redis.cache.RedisCacheService
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
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlin.math.log

@Service
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository,
    private val availableSeatRepository: AvailableSeatRepository,
    private val redisCacheService: RedisCacheService,
) : TicketService {
    companion object {
        val logger = LoggerFactory.getLogger(TicketServiceImpl::class.java)
    }

//    @PubSubLock
    @Transactional
    override fun createTicket(memberId: Long, request: CreateTicketRequest) {
        val event = eventRepository.findByIdOrNull(request.eventId)
            ?: throw ModelNotFoundException("event", request.eventId)

        Hibernate.initialize(event.availableSeats)   // 컬렉션을 명시적으로 초기화 ( LAZY 모드 )

        // 예약 날짜 체크
        if (request.date < event.startDate || request.date > event.endDate || request.date < LocalDate.now()){
            throw TicketReservationFailedException("예매일(${request.date})이 올바르지 않습니다.")
        }

        // 좌석 예약 가능 상태 확인
        val availableSeat = event.availableSeats.find {
            it.date == request.date && it.bookable == Bookable.OPEN
        } ?:let{
            throw TicketReservationFailedException("예매일(${request.date}) 의 예약이 불가능한 상태 입니다.")
        }

        // 예약 가능 좌석 선별
       request.seatList.map{ seat ->
           //캐싱 체크
           val key = "${request.eventId}_${request.date}_${seat.ticketGrade}_${seat.seatNumber}"
            if(redisCacheService.chkCache(CacheTarget.TICKET, key))
                throw TicketReservationFailedException("${seat.seatNumber} 번 좌석은 선택할 수 없습니다. ( 이미 예약된 좌석 in Cache )")

           //DB 체크
           val isReserved = ticketRepository.chkTicket(
                request.eventId,
                request.date,
                seat.ticketGrade,
                seat.seatNumber
           )
           if (isReserved != null) {
               throw TicketReservationFailedException("${seat.seatNumber} 번 좌석은 선택할 수 없습니다. ( 이미 예약된 좌석 in DB )")
           }
        }

        // 티켓 생성
        request.seatList.map{seat ->

            val member = memberRepository.findByIdOrNull(memberId)
                ?: throw ModelNotFoundException("member", memberId)

            val ticket = Ticket(
                date = request.date,
                grade = seat.ticketGrade,
                seatNo = seat.seatNumber,
                event = event,
                member = member,
                price = when (seat.ticketGrade) {
                    TicketGrade.R -> event.price!!.seatRPrice
                    TicketGrade.S -> event.price!!.seatSPrice
                    TicketGrade.A -> event.price!!.seatAPrice
                },
                place = event.place.name
            )

            ticketRepository.save( ticket )

            // 캐시에 등록
            redisCacheService.putCache(CacheTarget.TICKET,
                "${request.eventId}_${request.date}_${seat.ticketGrade}_${seat.seatNumber}",
                TicketResponse.from(ticket))

            // 좌석 예약 수 수정
            availableSeat.increaseSeat(seat.ticketGrade)
            if (availableSeat.isFull()) {
                availableSeat.close()
            }
            availableSeatRepository.save(availableSeat)
        }
        eventRepository.save(event)
    }

    // RedisCacheService 로 이동
//    fun chkTicketCache(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): Boolean {
//        logger.info("캐시의 티켓 확인 시작")
//        val cache = cacheManager.getCache("tickets")
//        logger.info("Cache : $cache")
//        val key = "${eventId}_${date}_${grade}_${seatNo}"
//        val ticket = cache?.get(key) // ticket : TicketResponse 상태
//
//        if (ticket != null) {    // 캐시에 일치하는 키 있을 때
//            logger.info("Cache hit for ticket: $key")
//            logger.info("Ticket in Cache : $ticket")
//            logger.info("이미 예매된 티켓입니다. ( in cache ) ")
//            return true
//        } else {   // 캐시에 일치하는 키 없을 때
//            logger.info("Cache miss for ticket: $key")
//            return false
//        }
//    }

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
//    override fun updateTicketStatus(ticketId: Long, ticketStatus: TicketStatus) {
//        val ticket = ticketRepository.findByIdOrNull(ticketId)
//            ?: throw ModelNotFoundException("Ticket", ticketId)
//
//        ticket.switchTicketStatus(ticketStatus)
//
//        ticketRepository.save(ticket)
//    }

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
                    ticketRepository.delete(it)
                }
                else
                    throw InvalidCredentialException("")
            }
    }

    override fun deleteTicket(ticketId: Long) {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)

        // TODO() availableSeat 증감 필요. cancel 과 합칠 수 있을지 확인

        ticketRepository.delete(ticket)
    }
}
