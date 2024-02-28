package com.codersgate.ticketraider.domain.event.dto.price

import com.codersgate.ticketraider.domain.event.model.Event

class PriceResponse (
    val event: Event,
    val rPrice: Int,
    val sPrice: Int,
    val aPrice: Int,
)