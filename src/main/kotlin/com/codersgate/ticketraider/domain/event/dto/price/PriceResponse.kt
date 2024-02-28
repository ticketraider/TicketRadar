package com.codersgate.ticketraider.domain.event.dto.price

import com.codersgate.ticketraider.domain.event.model.price.Price

class PriceResponse (
    val rPrice: Int,
    val sPrice: Int,
    val aPrice: Int,
){
    companion object {
        fun from(price: Price): PriceResponse {
            return PriceResponse(
                rPrice = price.rPrice,
                sPrice = price.sPrice,
                aPrice = price.aPrice
            )
        }
    }
}