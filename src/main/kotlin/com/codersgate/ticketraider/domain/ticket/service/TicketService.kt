package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.ticket.dto.CheckTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface TicketService {

    fun createTicket(userPrincipal: UserPrincipal, createTicketRequest: CreateTicketRequest)

    fun getTicketById(ticketId: Long): TicketResponse

    fun chkTicketCache(eventId:Long, date: LocalDate, grade:TicketGrade, seatNo:Int) : Ticket?

    fun deleteTicket(ticketId: Long, user: UserPrincipal)

    fun getTicketListByUserId(user: UserPrincipal , pageable: Pageable): Page<TicketResponse>
}