package com.codersgate.ticketraider.domain.event.dto.seat

import com.codersgate.ticketraider.domain.event.model.seat.AvailableSeat
import java.time.LocalDate

data class SeatResponse(
    val date: LocalDate,
    val totalSeat: Int,
    val seatR: Int,
    val seatS: Int,
    val seatA: Int,
) {
    companion object {
        fun from(availableSeat: AvailableSeat): SeatResponse {
            return SeatResponse(
                date = availableSeat.date,
                totalSeat = availableSeat.totalSeat,
                seatR = availableSeat.seatR,
                seatS = availableSeat.seatS,
                seatA = availableSeat.seatA
            )
        }
    }
}