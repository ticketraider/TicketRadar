package com.codersgate.ticketraider.domain.ticket.dto

import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

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
    val memberId: Long, //직접 입력할 여지를 주는것보단 UserPrincipal로 넘기면 어떨까 생각함
) {

    val list = mutableListOf<Pair<TicketGrade,Int>>()
}
