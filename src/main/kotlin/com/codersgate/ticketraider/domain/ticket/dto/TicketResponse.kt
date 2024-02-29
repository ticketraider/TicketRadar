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
    val eventName: String,
    val memberName: String,
) {
    companion object {
        fun from(ticket: Ticket): TicketResponse {
            val price = when(ticket.grade) {
                TicketGrade.R -> ticket.event.price!!.seatRPrice
                TicketGrade.S -> ticket.event.price!!.seatSPrice
                TicketGrade.A -> ticket.event.price!!.seatAPrice
            }
            return TicketResponse(
                id = ticket.id!!,
                seatNo = ticket.seatNo,
                price = price,
                grade = ticket.grade,
                date = ticket.date,
                eventName = ticket.event.title,
                memberName = ticket.member.nickname
            )
        }
    }
}
