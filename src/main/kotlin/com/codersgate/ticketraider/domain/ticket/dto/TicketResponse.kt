package com.codersgate.ticketraider.domain.ticket.dto

import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import java.util.*

data class TicketResponse(
    val id: Long,
    val seatNo: Int,
    val price: Int,
    val grade: TicketGrade,
    val date: Date,
    val eventId: Long,
    val memberId: Long,
) {
    companion object {
        fun from(ticket: Ticket): TicketResponse {
            return TicketResponse(
                id = ticket.id!!,
                seatNo = ticket.seatNo,
                price = ticket.price,
                grade = ticket.grade,
                date = ticket.date,
                eventId = ticket.event.id!!,
                memberId = ticket.member.id!!
            )
        }
    }
}
