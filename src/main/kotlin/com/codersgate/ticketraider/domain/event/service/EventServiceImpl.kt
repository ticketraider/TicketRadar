package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.category.repository.CategoryRepository
import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.model.seat.Seat
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.repository.price.PriceRepository
import com.codersgate.ticketraider.domain.event.repository.seat.SeatRepository
import com.codersgate.ticketraider.domain.place.repository.PlaceRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class EventServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val priceRepository: PriceRepository,
    private val seatRepository: SeatRepository,
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
            val seat = eventRequest.toSeat(event,place,date.plusDays(i.toLong()))
            event.seat.add(seat)
            seatRepository.save(seat)
        }
    }
    @Transactional
    override fun updateEvent(eventId: String, eventRequest: EventRequest) {
        val date = eventRequest.startDate
        val duration = eventRequest.endDate.compareTo(eventRequest.startDate)
        for (i in 0..duration) {
            println(date)
            println(duration)
        }
    }

    @Transactional
    override fun deleteEvent(eventId: Long) {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        eventRepository.delete(event)
    }

    override fun getEventList(): List<EventResponse> {
//        val eventList = eventRepository.findAll()
//        return eventList.map { EventResponse.from( it ) }
        TODO()
    }

    override fun getEvent(eventId: Long): EventResponse {
//        val event = eventRepository.findByIdOrNull(eventId)
//            ?: throw ModelNotFoundException("Event", eventId)
//        return EventResponse.from(event)
        TODO()
    }

//    override fun getPaginatedEventList(pageable: Pageable, status: String?): Page<EventResponse>? {
//        return  eventRepository.findByPageable(pageable).map{EventResponse.from(it)}
//    }
}