package com.codersgate.ticketraider.domain.event.repository.seat

import com.codersgate.ticketraider.domain.event.model.seat.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatRepository : JpaRepository<Seat, Long>{
    fun findAllByEventId(eventId: Long): List<Seat?>
}