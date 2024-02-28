package com.codersgate.ticketraider.domain.event.dto.price

import com.codersgate.ticketraider.domain.event.model.price.Price

data class PriceResponse (
    val seatRPrice: Int,
    val seatSPrice: Int,
    val seatAPrice: Int,
){
    companion object {
        fun from(price: Price): PriceResponse {
            return PriceResponse(
                seatRPrice = price.seatRPrice,
                seatSPrice = price.seatSPrice,
                seatAPrice = price.seatAPrice
            )
        }
    }
}