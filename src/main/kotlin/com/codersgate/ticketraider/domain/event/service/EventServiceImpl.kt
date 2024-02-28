package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.category.repository.CategoryRepository
import com.codersgate.ticketraider.domain.event.dto.CreateEventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.price.Price
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

@Service
class EventServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val priceRepository: PriceRepository,
    private val seatRepository: SeatRepository,
    private val placeRepository: PlaceRepository
) : EventService {
    override fun createEvent(
        categoryId: Long,
        eventRequest: CreateEventRequest,
    ) {
        val category = categoryRepository.findByIdOrNull(categoryId)
            ?: throw ModelNotFoundException("category", categoryId)
        val place = placeRepository.findPlaceByName(eventRequest.place)
            ?: throw ModelNotFoundException("place", 0)
        val price = Price(
            seatRPrice = eventRequest.seatRPrice,
            seatSPrice = eventRequest.seatSPrice,
            seatAPrice = eventRequest.seatAPrice
        )
        val event = Event(
            posterImage = eventRequest.posterImage,
            title = eventRequest.title,
            eventInfo = eventRequest.eventInfo,
            startDate = eventRequest.startDate,
            endDate = eventRequest.endDate,
            place = place,
            category = category,
            price = price
        )
        price.event = event
        eventRepository.save(event)
        priceRepository.save(price)
        //place 네임 기반으로 place리포지토리에서 조회후 넣어주기
        //startDate와 endDate를 기반으로 날짜 계산후 for반복문 완성해주기
        for (i in 1..3) {
            val seat = Seat(
                event = event,
                date = LocalDate.now(),//여기도 알맞은 날짜 넣도록하기
                seatR = place.seatR,
                seatS = place.seatS,
                seatA = place.seatA
            )
            event.seat.add(seat)
            seatRepository.save(seat)
        }

    }
//    @Transactional
//    override fun updateEvent(eventId: Long, request: UpdateEventRequest) {
//        val event = eventRepository.findByIdOrNull(eventId)
//            ?: throw ModelNotFoundException("Event", eventId)
//        Event(
//            posterImage = request.posterImage,
//            title = request.title,
//            eventInfo = request.eventInfo,
//            startDate = request.startDate,
//            endDate = request.endDate
//        )
//        eventRepository.save(event)
//        TODO()
//    }

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