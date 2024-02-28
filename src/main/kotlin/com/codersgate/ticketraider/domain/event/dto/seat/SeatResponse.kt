package com.codersgate.ticketraider.domain.event.dto.seat

import com.codersgate.ticketraider.domain.event.model.seat.Seat
import java.time.LocalDate

data class SeatResponse(
    val date: LocalDate,
    val totalSeat: Int,
    val rSeat: Int,
    val sSeat: Int,
    val aSeat: Int,
){
    companion object {
        fun from(seat: Seat): SeatResponse {
            return SeatResponse(
                date = seat.date,
                totalSeat = seat.totalSeat,
                rSeat = seat.rSeat,
                sSeat = seat.sSeat,
                aSeat = seat.aSeat
            )
        }
    }
}