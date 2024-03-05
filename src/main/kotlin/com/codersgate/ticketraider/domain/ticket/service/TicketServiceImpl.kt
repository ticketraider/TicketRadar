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
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class TicketServiceImpl(
    val ticketRepository: TicketRepository,
    val memberRepository: MemberRepository,
    val eventRepository: EventRepository,
    val cacheManager: CacheManager,
): TicketService {

    companion object{
        val logger = LoggerFactory.getLogger(TicketServiceImpl::class.java)
    }

//    @Cacheable(cacheNames = ["tickets"],
//        key="#createTicketRequest.eventId + '_' " +
//                "+ #createTicketRequest.date + '_' " +
//                "+ #createTicketRequest.grade + '_' " +
//                "+ #createTicketRequest.seatNo"
//    )
    override fun createTicket(createTicketRequest: CreateTicketRequest) {

        chkTicketCache(createTicketRequest.eventId, createTicketRequest.date, createTicketRequest.grade, createTicketRequest.seatNo)
//            ?: let {
        val event = eventRepository.findByIdOrNull(createTicketRequest.eventId)
            ?: throw ModelNotFoundException("event", createTicketRequest.eventId)
        val member = memberRepository.findByIdOrNull(createTicketRequest.memberId)
            ?: throw ModelNotFoundException("member", createTicketRequest.memberId)
        ticketRepository.save(
            Ticket(
                date = createTicketRequest.date,
                grade = createTicketRequest.grade,
                seatNo = createTicketRequest.seatNo,
                event = event,
                member = member,
                price = when (createTicketRequest.grade) {
                    TicketGrade.R -> event.price!!.seatRPrice
                    TicketGrade.S -> event.price!!.seatSPrice
                    TicketGrade.A -> event.price!!.seatAPrice
                },
                ticketStatus = TicketStatus.UNPAID,
                place = event.place.toString()
            )
        ).also{
            val cache = cacheManager.getCache("tickets")
            val key = "$createTicketRequest.eventId" +
                    "_${createTicketRequest.date}_$createTicketRequest.grade" +
                    "_$createTicketRequest.seatNo"
            cache?.put(key, it)
        }
    }


//    @Cacheable(cacheNames = ["tickets"],
//        key="#createTicketRequest.eventId + '_' " +
//                "+ #createTicketRequest.date + '_' " +
//                "+ #createTicketRequest.grade + '_' " +
//                "+ #createTicketRequest.seatNo"
//    )
    override fun chkTicketCache(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): Ticket? {
        logger.info("캐시의 티켓 확인")
        val cache = cacheManager.getCache("tickets")
        val key = "$eventId" +
                "_${date}_$grade" +
                "_$seatNo"
        var ticket = cache?.get(key) as? Ticket

        if (ticket != null) {
            logger.info("Cache hit for ticket: $key")
            logger.info("Ticket in Cache : $ticket")
            throw IllegalAccessException("이미 예매된 티켓입니다. ( in cache ) ")
        } else {
            logger.info("Cache miss for ticket: $key")
            logger.info("레포지토리의 티켓 확인")
            ticket = ticketRepository.chkTicket(eventId, date, grade, seatNo)
            if ( ticket != null ) {
                cache?.put(key, ticket)

//                addTicketInCache(ticket, eventId, date, grade, seatNo)    // 예매된 티켓인데 캐시 저장 안되어있을 경우 캐시에 다시 등록
                throw IllegalAccessException("이미 예매된 티켓입니다. ( in repository ) ")
            }
            else
                return null // 캐시에 해당 키에 대한 값이 없으면 null을 반환하도록 함
        }
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