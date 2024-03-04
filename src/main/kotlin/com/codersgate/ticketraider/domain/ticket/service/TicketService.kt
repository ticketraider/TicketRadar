package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TicketService {

    fun createTicket(createTicketRequest: CreateTicketRequest)

    fun getTicketById(ticketId: Long): TicketResponse

    fun deleteTicket(ticketId: Long, user: UserPrincipal)

    fun getTicketListByUserId(user: UserPrincipal , pageable: Pageable): Page<TicketResponse>
}