package com.codersgate.ticketraider.domain.event.repository

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.place.model.Place
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface EventRepository : JpaRepository<Event, Long>, CustomEventRepository {
    fun findAllByPlaceId(placeId: Long): List<Event?>

    fun findByTitle(title:String) : Event?
    fun existsByPlaceAndStartDateAndEndDate(place: Place, startDate: LocalDate, endDate: LocalDate): Boolean
}