package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import org.springframework.stereotype.Service

@Service
interface EventService {

    fun createEvent(eventRequest: EventRequest)

    fun updateEvent(eventId: String, eventRequest: EventRequest)

    fun deleteEvent(eventId: Long)

    fun getEventList(): List<EventResponse>

    fun getEvent(eventId: Long): EventResponse

//    fun getPaginatedEventList(pageable: Pageable, status : String?) : Page<EventResponse>?
// 쿼리dsl 구현후 작성
}
