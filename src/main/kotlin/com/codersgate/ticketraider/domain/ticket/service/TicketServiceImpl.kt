package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.domain.ticket.entity.TicketStatus
import com.codersgate.ticketraider.domain.ticket.repository.TicketRepository
import com.codersgate.ticketraider.global.error.exception.InvalidCredentialException
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
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

@Service
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository,
    private val redissonClient: RedissonClient,
    private val cacheManager: CacheManager,
) : TicketService {
    companion object{
        val logger = LoggerFactory.getLogger(TicketServiceImpl::class.java)
    }
    override fun createTicket(userPrincipal: UserPrincipal, request: CreateTicketRequest) {

        // 락 생성
        val lockList = mutableListOf<RLock>()
        val count = request.seatList.size

        request.seatList.map{
            val lock = redissonClient.getLock(generateKey(request.eventId, request.date, it.ticketGrade, it.seatNumber))
            lockList.add(lock)
            //획득시도 시간, 락 점유 시간
            lock.tryLock(10, 1, TimeUnit.SECONDS)
        }

        val event = eventRepository.findByIdOrNull(request.eventId)
            ?: throw ModelNotFoundException("event", request.eventId)
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("member", userPrincipal.id)


        // 티켓 생성
        for (i in 0 until count) {
            // 캐시 , 레포지토리 체크
            if( ! chkTicketCache(request.eventId, request.date, request.seatList[i].ticketGrade, request.seatList[i].seatNumber) )
                continue

            ticketRepository.save(
                Ticket(
                    date = request.date,
                    grade = request.seatList[i].ticketGrade,
                    seatNo = request.seatList[i].seatNumber,
                    event = event,
                    member = member,
                    price = when(request.seatList[i].ticketGrade) {
                        TicketGrade.R -> event.price!!.seatRPrice
                        TicketGrade.S -> event.price!!.seatSPrice
                        TicketGrade.A -> event.price!!.seatAPrice
                    },
                    place = event.place.name
                )
            ).also{
                // 락 해제
                lockList[i].unlock()
                // 캐시에 추가
                putTicketCache(
                    request.eventId,
                    request.date,
                    request.seatList[i].ticketGrade,
                    request.seatList[i].seatNumber,
                    TicketResponse.from(it))
        }
    }
    }

    override fun chkTicketCache(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): Boolean {
        logger.info("캐시의 티켓 확인 시작")
        val cache = cacheManager.getCache("tickets")
        logger.info("Cache : $cache")
        val key = "$eventId" + "_$date" +"_$grade" + "_$seatNo"
        val ticket = cache?.get(key) // Response 상태

        // 캐시에 일치하는 키 있을 때
        if (ticket != null) {
            logger.info("Cache hit for ticket: $key")
            logger.info("Ticket in Cache : $ticket")
            logger.info("이미 예매된 티켓입니다. ( in cache ) ")
            return false
        }
        // 캐시에 일치하는 키 없을 때
        else {
            logger.info("Cache miss for ticket: $key")
            logger.info("레포지토리의 티켓 확인 시작")

            val ticketResponse = ticketRepository.chkTicket(eventId, date, grade, seatNo)?.let{TicketResponse.from(it)}

            if ( ticketResponse != null ) {
                putTicketCache(eventId, date, grade, seatNo, ticketResponse)    // 예매된 티켓인데 캐시 저장 안되어있을 경우 캐시에 다시 등록
                logger.info("이미 예매된 티켓입니다. ( in repository ) ")
                return false
            }
            else
                return true // 캐시에 해당 키에 대한 값이 없으면 null을 반환하도록 함
        }
    }
    fun putTicketCache(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int, ticketResponse: TicketResponse)
    {
        val cache = cacheManager.getCache("tickets")
        logger.info("Cache : $cache")
        val key = "$eventId" + "_$date" +"_$grade" + "_$seatNo"
        cache?.put(key, ticketResponse)
    }

    override fun getTicketById(ticketId: Long): TicketResponse {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        return TicketResponse.from(ticket)
    }
    override fun getTicketListByUserId(user: UserPrincipal, pageable: Pageable): Page<TicketResponse> {
        return ticketRepository.getListByUserId(pageable, user.id).map { TicketResponse.from(it) }
    }
    override fun updateTicket(ticketId: Long, ticketStatus: TicketStatus) {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)

        ticket.switchTicketStatus(ticketStatus)

        ticketRepository.save(ticket)
    }

    override fun deleteTicket(ticketId: Long, user: UserPrincipal) {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        if (ticket.member.id!! != user.id) {
            throw InvalidCredentialException("")
        }
        ticketRepository.delete(ticket)
    }




}
private fun generateKey(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): String {
    val key = "ID : ${eventId}, $date : ${grade}-${seatNo}"
    return key
}