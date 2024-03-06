package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.category.repository.CategoryRepository
import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.repository.price.PriceRepository
import com.codersgate.ticketraider.domain.event.repository.seat.AvailableSeatRepository
import com.codersgate.ticketraider.domain.place.repository.PlaceRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.Pageable

@Service
class EventServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val priceRepository: PriceRepository,
    private val availableSeatRepository: AvailableSeatRepository,
    private val placeRepository: PlaceRepository
) : EventService {
    override fun createEvent(eventRequest: EventRequest) {
        val category = categoryRepository.findByIdOrNull(eventRequest.categoryId)
            ?: throw ModelNotFoundException("category", eventRequest.categoryId)
        val place = placeRepository.findPlaceByName(eventRequest.place)
            ?: throw ModelNotFoundException("place", 0)//예외 추가 필요함
        val (price, event) = eventRequest.toPriceAndEvent(category, place)
        event.price = price
        eventRepository.save(event)
        priceRepository.save(price)

        val date = eventRequest.startDate
        val duration = eventRequest.endDate.compareTo(eventRequest.startDate)
        for (i in 0..duration) {
            val seat = eventRequest.toAvailableSeat(event, place, date.plusDays(i.toLong()))
            availableSeatRepository.save(seat)
        }
    }


    override fun updateEvent(eventId: Long, eventRequest: EventRequest) {
        var event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        var price = priceRepository.findByEventId(eventId)
            ?: throw ModelNotFoundException("Price", eventId)
        val category = categoryRepository.findByIdOrNull(eventRequest.categoryId)
            ?: throw ModelNotFoundException("category", eventRequest.categoryId)
        val place = placeRepository.findPlaceByName(eventRequest.place)
            ?: throw ModelNotFoundException("place", 0)

        if (eventRequest.startDate != event.startDate || eventRequest.endDate != event.endDate) {
            availableSeatRepository.findAllByEventId(event.id!!).map { availableSeatRepository.delete(it!!) }
            val date = eventRequest.startDate
            val duration = eventRequest.endDate.compareTo(eventRequest.startDate)
            for (i in 0..duration) {
                val seat = eventRequest.toAvailableSeat(event, place, date.plusDays(i.toLong()))
                availableSeatRepository.save(seat)
            }
        }

        val (newPrice, newEvent) = eventRequest.toPriceAndEvent(category, place)
        event.price = newPrice
        price = newPrice
        event = newEvent
    }

    @Transactional
    override fun deleteEvent(eventId: Long) {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        eventRepository.delete(event)
    }

    override fun getPaginatedEventList(pageable: Pageable, status: String?, categoryId: Long?): Page<EventResponse>? {
        var id = categoryId
        if(categoryId == null) {
            id = 0
        }
        val category = categoryRepository.findByIdOrNull(id)

        val eventList = eventRepository.findByPageable(pageable, category)
        return eventList.map { EventResponse.from(it) }
    }

    override fun getEvent(eventId: Long): EventResponse {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        return EventResponse.from(event)
    }
}