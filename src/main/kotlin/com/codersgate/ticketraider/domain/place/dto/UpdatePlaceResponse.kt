package com.codersgate.ticketraider.domain.place.dto

data class UpdatePlaceRequest(
    val name: String,
    val seatR: Int,
    val seatS: Int,
    val seatA: Int,
)