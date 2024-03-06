package com.codersgate.ticketraider.domain.ticket.dto

import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

class CheckTicketRequest  (
    @field:NotNull
    val date: LocalDate,
    @field:NotNull
    val grade: TicketGrade,
    @field:NotNull
    val seatNo: Int,
    @field:NotNull
    val eventId: Long,
) {

}
