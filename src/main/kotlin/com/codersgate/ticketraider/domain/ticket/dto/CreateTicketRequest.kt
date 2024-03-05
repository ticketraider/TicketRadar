package com.codersgate.ticketraider.domain.ticket.dto

import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateTicketRequest(
    @field:NotNull // 어떻게 validation?
    val date: LocalDate,
    @field:NotNull
    val grade: TicketGrade,
    @field:NotNull
    val seatNo: Int,
    @field:NotNull
    val eventId: Long,
    @field:NotNull
    val memberId: Long
) {

}
