package com.codersgate.ticketraider.domain.event.dto.price

data class UpdatePriceRequest (
    val rPrice: Int,
    val sPrice: Int,
    val aPrice: Int,
)