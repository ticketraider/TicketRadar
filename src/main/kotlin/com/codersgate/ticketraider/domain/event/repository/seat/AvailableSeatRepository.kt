package com.codersgate.ticketraider.domain.event.repository.seat

import com.codersgate.ticketraider.domain.event.model.seat.AvailableSeat
import org.springframework.data.jpa.repository.JpaRepository

interface AvailableSeatRepository : JpaRepository<AvailableSeat, Long> {
    fun findAllByEventId(eventId: Long): List<AvailableSeat?>
}