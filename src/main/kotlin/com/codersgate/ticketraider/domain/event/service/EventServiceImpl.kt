package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.event.dto.CreateEventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.dto.UpdateEventRequest
import com.codersgate.ticketraider.domain.event.model.Bookable
import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.error.exception.ModelNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.ui.Model

@Service
class EventServiceImpl(
    private val eventRepository: EventRepository
) : EventService {
    override fun createEvent(request: CreateEventRequest) {
        val event = Event(
            posterImage = request.posterImage,
            title = request.title,
            eventInfo = request.eventInfo,
            startDate = request.startDate,
            endDate = request.endDate,
        )
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