package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
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
) : TicketService {
    companion object{
        val logger = LoggerFactory.getLogger(TicketServiceImpl::class.java)
    }
    override fun createTicket(userPrincipal: UserPrincipal, createTicketRequest: CreateTicketRequest) {
        chkTicketCache(createTicketRequest.eventId, createTicketRequest.date, createTicketRequest.grade, createTicketRequest.seatNo)
        val event = eventRepository.findByIdOrNull(createTicketRequest.eventId)
            ?: throw ModelNotFoundException("event", createTicketRequest.eventId)
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("member", userPrincipal.id)

        val lockList = mutableListOf<RLock>()
        val count = createTicketRequest.seatList.size
        for (i in 0 until count) {
            val lock = redissonClient.getLock(generateKey(createTicketRequest))
            lockList.add(lock)
            //획득시도 시간, 락 점유 시간
            lock.tryLock(10, 1, TimeUnit.SECONDS)
        }
        for (i in 0 until count) {
            //키마다 캐시안에 넣는 작업 캐시안에 있는지 없는지
            //DB존재 유무파악
            ticketRepository.save(
                Ticket(
                    date = createTicketRequest.date,
                    grade = createTicketRequest.seatList[i].first,
                    seatNo = createTicketRequest.seatList[i].second,
                    event = event,
                    member = member,
                    price = when(createTicketRequest.seatList[i].first) {
                        TicketGrade.R -> event.price!!.seatRPrice
                        TicketGrade.S -> event.price!!.seatSPrice
                        TicketGrade.A -> event.price!!.seatAPrice
                    },
                    place = event.place.name
                )
            )
        ).also{
            lockList[i].unlock()
            putTicketCache(
                createTicketRequest.eventId,
                createTicketRequest.date,
                createTicketRequest.grade,
                createTicketRequest.seatNo,
                TicketResponse.from(it))
        }
    }

    override fun chkTicketCache(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): Ticket? {
        logger.info("캐시의 티켓 확인")
        val cache = cacheManager.getCache("tickets")
        val key = "tickets::$eventId" + "_$date" +"_$grade" + "_$seatNo"
        val ticket = cache?.get(key) // Response 상태

        if (ticket != null) {
            logger.info("Cache hit for ticket: $key")
            logger.info("Ticket in Cache : $ticket")
            throw IllegalAccessException("이미 예매된 티켓입니다. ( in cache ) ")
        } else {
            logger.info("Cache miss for ticket: $key")
            logger.info("Ticket in Cache : $ticket")
            logger.info("레포지토리의 티켓 확인")
            val ticketResponse = ticketRepository.chkTicket(eventId, date, grade, seatNo)?.let{TicketResponse.from(it)}
            if ( ticketResponse != null ) {
                putTicketCache(eventId, date, grade, seatNo, ticketResponse)    // 예매된 티켓인데 캐시 저장 안되어있을 경우 캐시에 다시 등록
                throw IllegalAccessException("이미 예매된 티켓입니다. ( in repository ) ")
            }
            else
                return null // 캐시에 해당 키에 대한 값이 없으면 null을 반환하도록 함
        }
    }
    fun putTicketCache(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int, ticketResponse: TicketResponse)
    {
        val cache = cacheManager.getCache("tickets")
        val key = "$eventId" + "_$date" +"_$grade" + "_$seatNo"
        cache?.put(key, ticketResponse)
    }

    override fun getTicketById(ticketId: Long): TicketResponse {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        return TicketResponse.from(ticket)
    }

    override fun deleteTicket(ticketId: Long, user: UserPrincipal) {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        if (ticket.member.id!! != user.id) {
            throw InvalidCredentialException("")
        }
        ticketRepository.delete(ticket)
    }



    override fun getTicketListByUserId(user: UserPrincipal, pageable: Pageable): Page<TicketResponse> {
        return ticketRepository.getListByUserId(pageable, user.id).map { TicketResponse.from(it) }
    }
}
private fun generateKey(request: CreateTicketRequest): String {
    val key = "ID : ${request.eventId}, ${request.date} : ${request.seatList[0].first}-${request.seatList[0].second}"
    return key
}