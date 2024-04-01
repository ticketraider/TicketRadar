package com.codersgate.ticketraider.domain.ticket.dto

import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.domain.ticket.entity.TicketStatus
import java.time.LocalDate

data class TicketResponse(
    val id: Long,
    val seatNo: Int,
    val price: Int,
    val grade: TicketGrade,
    val date: LocalDate,
    val place: String,
    val address: String,
    val ticketStatus: TicketStatus,
    val eventName: String,
    val eventId: Long,
    val memberNickname: String,
) {
    companion object {
        fun from(ticket: Ticket): TicketResponse {

            return TicketResponse(
                id = ticket.id!!,
                seatNo = ticket.seatNo,
                price = ticket.price,
                grade = ticket.grade,
                date = ticket.date,
                place = ticket.place,
                address = ticket.address,
                ticketStatus = ticket.ticketStatus,
                eventName = ticket.event.title,
                eventId = ticket.event.id!!,
                memberNickname = ticket.member.nickname,
            )
        }
    }
}
