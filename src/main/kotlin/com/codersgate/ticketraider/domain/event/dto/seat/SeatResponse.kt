package com.codersgate.ticketraider.domain.event.dto.seat

import com.codersgate.ticketraider.domain.event.model.seat.Seat
import java.time.LocalDate

data class SeatResponse(
    val date: LocalDate,
    val totalSeat: Int,
    val seatR: Int,
    val seatS: Int,
    val seatA: Int,
){
    companion object {
        fun from(seat: Seat): SeatResponse {
            return SeatResponse(
                date = seat.date,
                totalSeat = seat.totalSeat,
                seatR = seat.seatR,
                seatS = seat.seatS,
                seatA = seat.seatA
            )
        }
    }
}