package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile

@Service
interface EventService {
    fun createEvent(eventRequest: EventRequest)

    fun updateEvent(eventId: Long, eventRequest: EventRequest)

    fun deleteEvent(eventId: Long)

    fun getPaginatedEventList(pageable: Pageable, status : String?, categoryId: Long?) : Page<EventResponse>?

    fun getEvent(eventId: Long): EventResponse

    fun uploadImage(file: MultipartFile?): String
}
