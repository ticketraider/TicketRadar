package com.codersgate.ticketraider.domain.ticket.dto

import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import java.time.LocalDate

data class TicketResponse(
    val id: Long,
    val seatNo: Int,
    val price: Int,
    val grade: TicketGrade,
    val date: LocalDate,
    val place: String,
    val eventName: String,
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
                eventName = ticket.event.title,
                memberNickname = ticket.member.nickname,
                place = ticket.place
            )
        }
    }
}
