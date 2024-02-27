package com.codersgate.ticketraider.domain.place.dto

data class CreatePlaceRequest(
    val name: String,
    val rSeat: Int,
    val sSeat: Int,
    val aSeat: Int,
)