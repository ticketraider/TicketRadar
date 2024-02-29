package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.repository.TicketRepository
import com.codersgate.ticketraider.global.error.exception.InvalidCredentialException
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl(
    val ticketRepository: TicketRepository,
    val memberRepository: MemberRepository,
    val eventRepository: EventRepository
): TicketService {
    override fun createTicket(createTicketRequest: CreateTicketRequest) {
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
                member = member
            )
        )
    }

    override fun getTicketById(ticketId: Long): TicketResponse {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        return TicketResponse.from(ticket)
    }


    override fun deleteTicket(ticketId: Long, user: UserPrincipal) {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        if(ticket.member.id!! != user.id) {
            throw InvalidCredentialException("")
        }
        ticketRepository.delete(ticket)
    }

    override fun getTicketListByUserId(user: UserPrincipal, pageable: Pageable): Page<TicketResponse> {
        return ticketRepository.getListByUserId(pageable, user.id).map { TicketResponse.from(it) }
    }
}