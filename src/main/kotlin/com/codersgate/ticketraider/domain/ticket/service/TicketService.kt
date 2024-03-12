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
    //fun chkTicketCache(eventId:Long, date: LocalDate, grade:TicketGrade, seatNo:Int) : Boolean?

    fun getAllTicketList(pageable:Pageable, memberId:Long?, eventId:Long?) : Page<TicketResponse>
    fun getTicketById(ticketId: Long): TicketResponse
    fun getTicketListByUserId(userPrincipal: UserPrincipal , pageable: Pageable): Page<TicketResponse>
    fun updateTicketStatus(ticketId: Long, ticketStatus: TicketStatus)
    fun chkExpiredTickets()
    fun makePayment(userPrincipal: UserPrincipal, ticketIdList : MutableList<Long>) : MutableList<TicketResponse>

    fun cancelTicket(ticketId: Long, userPrincipal: UserPrincipal)
    fun deleteTicket(ticketId: Long)
}