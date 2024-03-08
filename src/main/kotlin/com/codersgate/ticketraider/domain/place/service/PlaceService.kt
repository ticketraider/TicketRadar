package com.codersgate.ticketraider.domain.place.service

import com.codersgate.ticketraider.domain.place.dto.PlaceRequest
import com.codersgate.ticketraider.domain.place.dto.PlaceResponse

interface PlaceService {

    fun createPlace(request: PlaceRequest)

    fun updatePlace(placeId: Long, request: PlaceRequest)

    fun deletePlace(placeId: Long)

    fun getPlaceList(): List<PlaceResponse>

    fun getPlace(placeId: Long): PlaceResponse
}