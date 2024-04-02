package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.dto.price.PriceResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
interface EventService {

    fun getPrice(eventId: Long): PriceResponse
    fun createEvent(eventRequest: EventRequest)
    fun updateEvent(eventId: Long, eventRequest: EventRequest)

    fun deleteEvent(eventId: Long)

    fun getPaginatedEventList(
        pageable: Pageable,
        sortStatus: String?,
        searchStatus: String?,
        category: String?,
        keyword: String?
    ): Page<EventResponse>?

    fun getEvent(eventId: Long): EventResponse

    fun uploadImage(file: MultipartFile?): String
}
