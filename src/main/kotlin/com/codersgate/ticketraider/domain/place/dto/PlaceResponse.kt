package com.codersgate.ticketraider.domain.place.dto

import com.codersgate.ticketraider.domain.place.model.Place

data class PlaceResponse(
    val name: String,
    val totalSeat: Int,
    val rSeat: Int,
    val sSeat: Int,
    val aSeat: Int,
) {
    companion object {
        fun from(place: Place): PlaceResponse {
            return PlaceResponse(
                name = place.name,
                rSeat = place.rSeat,
                sSeat = place.sSeat,
                aSeat = place.aSeat,
                totalSeat = (place.rSeat + place.sSeat + place.aSeat)
            )
        }
    }
}