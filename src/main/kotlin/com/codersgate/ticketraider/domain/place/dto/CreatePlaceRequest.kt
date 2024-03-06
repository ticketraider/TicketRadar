package com.codersgate.ticketraider.domain.place.dto

data class CreatePlaceRequest(
    val name: String,
    val seatR: Int,
    val seatS: Int,
    val seatA: Int,
    val address: String,
)