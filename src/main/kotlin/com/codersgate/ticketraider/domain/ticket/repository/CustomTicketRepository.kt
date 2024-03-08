package com.codersgate.ticketraider.domain.ticket.repository

import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.time.LocalDateTime

interface CustomTicketRepository {

    fun getAllTicketList(pageable: Pageable, memberId: Long?, eventId: Long?) : Page<Ticket>

    fun getListByUserId(pageable: Pageable, userId: Long): Page<Ticket>

    fun chkTicket(eventId:Long, date: LocalDate, grade: TicketGrade, seatNo:Int) : Ticket?
}