package com.codersgate.ticketraider.domain.event.service

import com.codersgate.ticketraider.domain.category.repository.CategoryRepository
import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.dto.price.PriceResponse
import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.repository.price.PriceRepository
import com.codersgate.ticketraider.domain.event.repository.seat.AvailableSeatRepository
import com.codersgate.ticketraider.domain.place.dto.PlaceResponse
import com.codersgate.ticketraider.domain.place.repository.PlaceRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import com.codersgate.ticketraider.global.infra.s3.S3Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class EventServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val priceRepository: PriceRepository,
    private val availableSeatRepository: AvailableSeatRepository,
    private val placeRepository: PlaceRepository,
    private val s3Service: S3Service
) : EventService {
    override fun getPrice(eventId: Long): PriceResponse {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        return PriceResponse.from(event.price!!)
    }

    @Transactional
    override fun createEvent(eventRequest: EventRequest) {
        val category = categoryRepository.findByIdOrNull(eventRequest.categoryId)
            ?: throw ModelNotFoundException("category", eventRequest.categoryId)
        val place = placeRepository.findPlaceByName(eventRequest.place)
            ?: throw ModelNotFoundException("place", 0)//예외 추가 필요함
        //시작일과 끝나는 일 비교후 false 시 예외처리
        check(eventRequest.startDate <= eventRequest.endDate) {
            "끝나는날짜는 시작날짜보다 빠를수 없습니다."
        }

        val (price, event) = eventRequest.toPriceAndEvent(category, place)
        event.posterImage =
            if(eventRequest.posterImage=="")
                "https://shorturl.at/kFIV0"
            else
                eventRequest.posterImage
        eventRepository.save(event)
        priceRepository.save(price)

        checkSeatForUpdateAndCreate(event, eventRequest, availableSeatRepository)
    }

    override fun uploadImage(file: MultipartFile?): String {
        return  if(file == null) { "" }
        else { s3Service.putObject(file) }
    }


    @Transactional
    override fun updateEvent(eventId: Long, eventRequest: EventRequest) {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        val price = priceRepository.findByEventId(eventId)
            ?: throw ModelNotFoundException("Price", eventId)
        val category = categoryRepository.findByIdOrNull(eventRequest.categoryId)
            ?: throw ModelNotFoundException("category", eventRequest.categoryId)
        val place = placeRepository.findPlaceByName(eventRequest.place)
            ?: throw ModelNotFoundException("place", 0)

        val orgStartDate = event.startDate
        val orgEndDate = event.endDate
        //시작일과 끝나는 일 비교후 false 시 예외처리
        check(eventRequest.startDate < eventRequest.endDate) {
            "끝나는날짜는 시작날짜보다 빠를수 없습니다."
        }

        //날짜 변동이 생겼는지 확인
        if (orgStartDate != event.startDate || orgEndDate != event.endDate) {
            checkSeatForUpdateAndCreate(event, eventRequest, availableSeatRepository)
        }

        event.let {
            it.title = eventRequest.title
            it.eventInfo = eventRequest.eventInfo
            it.startDate = eventRequest.startDate
            it.endDate = eventRequest.endDate
            it.place = place
            it.category = category

        }

        if(eventRequest.posterImage!=event.posterImage && eventRequest.posterImage!="")
            eventRequest.posterImage

        event.posterImage = eventRequest.posterImage

        price.let {
            it.seatRPrice = eventRequest.seatRPrice
            it.seatSPrice = eventRequest.seatSPrice
            it.seatAPrice = eventRequest.seatAPrice
            it.event = event
        }
        event.price = price

        priceRepository.save(price)
        eventRepository.save(event)

    }

    @Transactional
    override fun deleteEvent(eventId: Long) {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        eventRepository.delete(event)
    }

    override fun getPaginatedEventList(
        pageable: Pageable,
        sortStatus: String?,
        searchStatus: String?,
        category: String?,
        keyword: String?
    ): Page<EventResponse>? {

        val eventList = eventRepository.findByPageable(pageable, sortStatus, searchStatus, category, keyword)
        return eventList.map {
            val place = placeRepository.findByIdOrNull(it.place.id)
                ?: throw ModelNotFoundException("place", it.place.id)
            val price = priceRepository.findByIdOrNull(it.price!!.id)
                ?: throw ModelNotFoundException("price", it.price!!.id)
            EventResponse.from(it, PlaceResponse.from(place), PriceResponse.from(price)) }
    }

    override fun getEvent(eventId: Long): EventResponse {
        val event = eventRepository.findByIdOrNull(eventId)
            ?: throw ModelNotFoundException("Event", eventId)
        val place = placeRepository.findByIdOrNull(event.place.id)
            ?: throw ModelNotFoundException("place", event.place.id)
        val price = priceRepository.findByIdOrNull(event.price!!.id)
            ?: throw ModelNotFoundException("price", event.price!!.id)
        return EventResponse.from(event, PlaceResponse.from(place), PriceResponse.from(price))
    }
}

private fun checkSeatForUpdateAndCreate(event: Event, request: EventRequest, seatRepository: AvailableSeatRepository) {
    val date = event.startDate
    val duration = event.endDate.compareTo(event.startDate)
    //다른 이벤트가 존재하는지 체크
    for (i in 0..duration) {
        val seat = seatRepository.findByPlaceIdAndDate(event.place.id!!, date.plusDays(i.toLong()))
        if (seat != null) {
            check(seat.event!!.id == event.id) {
                "해당 날짜에 이미 다른 이벤트가 존재합니다."
            }
        }
    }
    //해당 이벤트id로 모든 Seat를 불러옴 > 정해둔 기간 외에 날짜는 삭제처리
    val seatList = seatRepository.findAllByEventId(event.id!!)
    seatList.map {
        if (it!!.date.isBefore(event.startDate) || it.date.isAfter(event.endDate)) {
            it.isDeleted = true
        }
    }
    //없는 seat는 생성함
    for (i in 0..duration) {
        val seat = seatRepository.findByEventIdAndDate(event.id!!, date.plusDays(i.toLong()))
        if (seat == null) {
            val newSeat = request.toAvailableSeat(event, date.plusDays(i.toLong()))
            seatRepository.save(newSeat)
        }
    }
}