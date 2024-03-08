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
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

        //시작일과 끝나는 일 비교후 false 시 예외처리
        check(eventRequest.startDate < eventRequest.endDate) {
            "끝나는날짜는 시작날짜보다 빠를수 없습니다."
        }

        val (price, event) = eventRequest.toPriceAndEvent(category, place)
        check(
            !eventRepository.existsByPlaceAndStartDateAndEndDate(
                place,
                eventRequest.startDate,
                eventRequest.endDate
            )
        ) {
            "이미 입력한 장소의 해당 날짜에 존재하는 Event가 있습니다."
        }
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
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        val price = priceRepository.findByEventId(eventId)
            ?: throw ModelNotFoundException("Price", eventId)
        val category = categoryRepository.findByIdOrNull(eventRequest.categoryId)
            ?: throw ModelNotFoundException("category", eventRequest.categoryId)
        val place = placeRepository.findPlaceByName(eventRequest.place)
            ?: throw ModelNotFoundException("place", 0)

        //시작일과 끝나는 일 비교후 false 시 예외처리
        check(eventRequest.startDate < eventRequest.endDate) {
            "끝나는날짜는 시작날짜보다 빠를수 없습니다."
        }

        if (eventRequest.startDate != event.startDate || eventRequest.endDate != event.endDate) {
            check(
                !eventRepository.existsByPlaceAndStartDateAndEndDate(
                    place,
                    eventRequest.startDate,
                    eventRequest.endDate
                )
            ) {
                "이미 입력한 장소의 해당 날짜에 존재하는 Event가 있습니다."
            }
            val (newPrice, newEvent) = eventRequest.toPriceAndEvent(category, place)
            price.let { newPrice }
            event.let { newEvent }
            val date = eventRequest.startDate
            val duration = eventRequest.endDate.compareTo(eventRequest.startDate)
            for (i in 0..duration) {
                val seat = availableSeatRepository.findByEventIdAndDate(eventId, date.plusDays(i.toLong()))
                if (seat == null) {
                    val newSeat = eventRequest.toAvailableSeat(event, place, date.plusDays(i.toLong()))
                    availableSeatRepository.save(newSeat)
                }
            }
        }


    }

    @Transactional
    override fun deleteEvent(eventId: Long) {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        eventRepository.delete(event)
    }

    override fun getPaginatedEventList(pageable: Pageable, status: String?, categoryId: Long?): Page<EventResponse>? {

        val eventList = eventRepository.findByPageable(pageable, categoryId, status)
        return eventList.map { EventResponse.from(it) }
    }

    override fun getEvent(eventId: Long): EventResponse {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        return EventResponse.from(event)
    }
}