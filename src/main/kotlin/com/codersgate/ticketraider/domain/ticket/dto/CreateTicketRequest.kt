package com.codersgate.ticketraider.domain.ticket.dto

import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateTicketRequest(
    @field:NotNull // 어떻게 validation?
    val date: LocalDate,
    @field:NotNull
    val eventId: Long,
) {
    val seatList = mutableListOf<Pair<TicketGrade,Int>>()

}
