package com.codersgate.ticketraider.domain.event.dto

data class CreateEventRequest(
    val posterImage: String,
    val title: String,
    val eventInfo: String,
    val startDate: String,
    val endDate: String,
    val place: String,
    val seatRPrice: Int,
    val seatSPrice: Int,
    val seatAPrice: Int,
)