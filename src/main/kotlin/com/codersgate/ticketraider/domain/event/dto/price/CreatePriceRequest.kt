package com.codersgate.ticketraider.domain.event.dto.price

data class CreatePriceRequest(
    val rPrice: Int,
    val sPrice: Int,
    val aPrice: Int,
)