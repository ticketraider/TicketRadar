package com.codersgate.ticketraider.domain.place.dto

data class UpdatePlaceRequest(
    val name: String,
    val rSeat: Int,
    val sSeat: Int,
    val aSeat: Int,
)