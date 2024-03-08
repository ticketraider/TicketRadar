package com.codersgate.ticketraider.domain.place.dto

import com.codersgate.ticketraider.domain.event.model.seat.AvailableSeat
import com.codersgate.ticketraider.domain.place.model.Place

data class PlaceRequest(
    val name: String,
    val seatR: Int,
    val seatS: Int,
    val seatA: Int,
    val address: String,
) {
    fun toPlace(): Place {
        val place = Place(
            name = name,
            seatR = seatR,
            seatS = seatS,
            seatA = seatA,
            address = address,
            totalSeat = seatR + seatS + seatA
        )
        return place
    }

    fun updateSeatByPlace(seat: AvailableSeat, place: Place): AvailableSeat {
        seat.totalSeat = place.totalSeat
        seat.seatR = place.seatR
        seat.seatS = place.seatS
        seat.seatA = place.seatA
        return seat
    }
}