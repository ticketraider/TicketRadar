package com.codersgate.ticketraider.domain.event.repository.seat

import com.codersgate.ticketraider.domain.event.model.seat.AvailableSeat
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AvailableSeatRepository : JpaRepository<AvailableSeat, Long> {
    fun findAllByEventId(eventId: Long): List<AvailableSeat?>

    fun findByPlaceId(placeId:Long): List<AvailableSeat?>

    fun findByEventIdAndDate(eventId: Long, date: LocalDate): AvailableSeat?

    fun deleteByEventId(eventId: Long)
}