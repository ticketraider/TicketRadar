package com.codersgate.ticketraider.domain.event.repository.price

import com.codersgate.ticketraider.domain.event.model.price.Price
import com.codersgate.ticketraider.domain.event.model.seat.AvailableSeat
import org.springframework.data.jpa.repository.JpaRepository

interface PriceRepository : JpaRepository<Price, Long> {
    fun findByEventId(eventId: Long): Price
}