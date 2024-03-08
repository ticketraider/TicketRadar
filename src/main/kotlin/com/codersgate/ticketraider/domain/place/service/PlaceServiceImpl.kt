package com.codersgate.ticketraider.domain.place.service

import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.repository.seat.AvailableSeatRepository
import com.codersgate.ticketraider.domain.place.dto.PlaceRequest
import com.codersgate.ticketraider.domain.place.dto.PlaceResponse
import com.codersgate.ticketraider.domain.place.repository.PlaceRepository
import com.codersgate.ticketraider.domain.ticket.repository.TicketRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PlaceServiceImpl(
    private val placeRepository: PlaceRepository,
    private val availableSeatRepository: AvailableSeatRepository,
    private val ticketRepository: TicketRepository
) : PlaceService {

    override fun createPlace(request: PlaceRequest) {
        val place = request.toPlace()
        placeRepository.save(place)

    }

    override fun updatePlace(placeId: Long, request: PlaceRequest) {
        val place = placeRepository.findByIdOrNull(placeId)
            ?: throw ModelNotFoundException("place", placeId)
        place.let { request.toPlace() }
        placeRepository.save(place)

        val seatList = availableSeatRepository.findByPlaceId(placeId)
        seatList.map {
            request.updateSeatByPlace(it!!, place)
            availableSeatRepository.save(it)
        }

        val ticketList = ticketRepository.findAllByPlaceId(placeId)
        ticketList.map {
            it!!.place = place.name
            ticketRepository.save(it)
        }
    }

    override fun deletePlace(placeId: Long) {
        val place = placeRepository.findByIdOrNull(placeId)
            ?: throw ModelNotFoundException("place", placeId)
        placeRepository.delete(place)
    }

    override fun getPlaceList(): List<PlaceResponse> {
        val placeList = placeRepository.findAll()
        return placeList.map { PlaceResponse.from(it) }
    }

    override fun getPlace(placeId: Long): PlaceResponse {
        val place = placeRepository.findByIdOrNull(placeId)
            ?: throw ModelNotFoundException("place", placeId)
        return PlaceResponse.from(place)
    }

    override fun getEventList(placeId: Long): List<EventResponse>? {
        return placeRepository.findByIdOrNull(placeId)
            ?.let {
                it.eventList.map { e -> EventResponse.from(e) }
            }
    }
}