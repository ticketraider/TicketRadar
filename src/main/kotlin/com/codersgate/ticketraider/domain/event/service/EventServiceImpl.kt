package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.event.dto.CreateEventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.dto.UpdateEventRequest
import com.codersgate.ticketraider.domain.event.dto.price.CreatePriceRequest
import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.price.Price
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.repository.price.PriceRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EventServiceImpl(
    private val eventRepository: EventRepository,
    private val priceRepository: PriceRepository,
) : EventService {
    override fun createEvent(createEventRequest: CreateEventRequest,createPriceRequest: CreatePriceRequest) {
        val price = Price(
            rPrice = createPriceRequest.rPrice,
            sPrice = createPriceRequest.sPrice,
            aPrice = createPriceRequest.aPrice
        )
        val event = Event(
            posterImage = createEventRequest.posterImage,
            title = createEventRequest.title,
            eventInfo = createEventRequest.eventInfo,
            startDate = createEventRequest.startDate,
            endDate = createEventRequest.endDate,
            price = price
        )
        price.event = event
        priceRepository.save(price)
        eventRepository.save(event)
    }

    override fun updateEvent(eventId: Long, request: UpdateEventRequest) {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        event.title = request.title
    }

    override fun deleteEvent(eventId: Long) {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        eventRepository.delete(event)
    }

    override fun getEventList(): List<EventResponse> {
        val eventList = eventRepository.findAll()
        return eventList.map { EventResponse.from(it) }
    }

    override fun getEvent(eventId: Long): EventResponse {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        return EventResponse.from(event)
    }

//    override fun getPaginatedEventList(pageable: Pageable, status: String?): Page<EventResponse>? {
//        return  eventRepository.findByPageable(pageable).map{EventResponse.from(it)}
//    }
}