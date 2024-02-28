package com.codersgate.ticketraider.domain.event.dto.seat

import java.time.LocalDate

data class CreateSeatRequest(
    val startDate: String,
    val endDate: String,
    val place: String,
)