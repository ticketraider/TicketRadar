package com.codersgate.ticketraider.domain.place.dto

import com.codersgate.ticketraider.domain.place.model.Place

data class PlaceResponse(
    val name: String,
    val totalSeat: Int,
    val seatR: Int,
    val seatS: Int,
    val seatA: Int,
) {
    companion object {
        fun from(place: Place): PlaceResponse {
            return PlaceResponse(
                name = place.name,
                seatR = place.seatR,
                seatS = place.seatS,
                seatA = place.seatA,
                totalSeat = (place.seatR + place.seatS + place.seatA)
            )
        }
    }
}