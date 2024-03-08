package com.codersgate.ticketraider.domain.ticket.dto

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateTicketRequest(
    @field : FutureOrPresent(message = "지난 일자는 예매가 불가능합니다!")
    val date: LocalDate,
    @field:NotNull
    val eventId: Long,
) {
    val seatList = mutableListOf<SeatInfo>()
}
