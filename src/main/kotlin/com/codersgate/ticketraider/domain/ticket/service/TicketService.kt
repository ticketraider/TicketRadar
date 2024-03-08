package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.ticket.dto.CheckTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.domain.ticket.entity.TicketStatus
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface TicketService {

    fun createTicket(userPrincipal: UserPrincipal, request: CreateTicketRequest)
    fun chkTicketCache(eventId:Long, date: LocalDate, grade:TicketGrade, seatNo:Int) : Boolean?
    fun getTicketById(ticketId: Long): TicketResponse
    fun getTicketListByUserId(user: UserPrincipal , pageable: Pageable): Page<TicketResponse>
    fun updateTicket(ticketId: Long, ticketStatus: TicketStatus)
    fun chkExpiredTickets()
    fun deleteTicket(ticketId: Long, user: UserPrincipal)
}