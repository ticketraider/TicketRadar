package com.codersgate.ticketraider.domain.ticket.dto

import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import jakarta.validation.constraints.NotNull
import java.util.*

data class CreateTicketRequest(
    @field:NotNull // 어떻게 validation?
    val date: Date,
    @field:NotNull
    val grade: TicketGrade,
    @field:NotNull
    val price: Int,
    @field:NotNull
    val seatNo: Int,
    @field:NotNull
    val eventId: Long,
    @field:NotNull
    val memberId: Long
) {

}
