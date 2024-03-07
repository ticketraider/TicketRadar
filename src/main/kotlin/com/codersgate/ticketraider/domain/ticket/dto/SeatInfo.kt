package com.codersgate.ticketraider.domain.ticket.dto

import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade

data class SeatInfo(
    val ticketGrade: TicketGrade,
    val seatNumber: Int
)
