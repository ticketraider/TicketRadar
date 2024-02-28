package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.event.dto.CreateEventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.dto.UpdateEventRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
interface EventService {

    fun createEvent(request : CreateEventRequest)

    fun updateEvent(eventId : Long, request : UpdateEventRequest)

    fun deleteEvent(eventId: Long)

    fun getEventList() : List<EventResponse>

    fun getEvent(eventId: Long) : EventResponse

//    fun getPaginatedEventList(pageable: Pageable, status : String?) : Page<EventResponse>?
// 쿼리dsl 구현후 작성
}
