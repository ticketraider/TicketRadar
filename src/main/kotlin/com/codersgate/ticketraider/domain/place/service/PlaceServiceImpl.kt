package com.codersgate.ticketraider.domain.place.service

import com.codersgate.ticketraider.domain.place.dto.CreatePlaceRequest
import com.codersgate.ticketraider.domain.place.dto.PlaceResponse
import com.codersgate.ticketraider.domain.place.dto.UpdatePlaceRequest
import com.codersgate.ticketraider.domain.place.model.Place
import com.codersgate.ticketraider.domain.place.repository.PlaceRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PlaceServiceImpl(
    private val placeRepository: PlaceRepository
) : PlaceService {

    override fun createPlace(request: CreatePlaceRequest) {
        val place = Place(
            name = request.name,
            rSeat = request.rSeat,
            sSeat = request.sSeat,
            aSeat = request.aSeat,
            totalSeat = (request.rSeat + request.sSeat + request.aSeat)
        )
        placeRepository.save(place)

    }

    override fun updatePlace(placeId: Long, request: UpdatePlaceRequest) {
        val place = placeRepository.findByIdOrNull(placeId)
            ?: throw ModelNotFoundException("place", placeId)
        place.name = request.name
        place.rSeat = request.rSeat
        place.sSeat = request.sSeat
        place.aSeat = request.aSeat
        place.totalSeat = (request.rSeat + request.sSeat + request.aSeat)
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
}