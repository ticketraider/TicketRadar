package com.codersgate.ticketraider.domain.event.dto.seat

data class UpdateSeatRequest(
    val startDate : String,
    val endDate : String,
    val place: String,
)