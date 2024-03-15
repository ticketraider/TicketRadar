package com.codersgate.ticketraider.domain.ticket.repository

import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface TicketRepository: JpaRepository<Ticket, Long>, CustomTicketRepository {
    fun findAllByMemberId(memberId : Long) : List<Ticket>
    fun findAllByEventId(eventId:Long) : List<Ticket?>

    fun existsByEventIdAndDateAndGradeAndSeatNo(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): Boolean
}