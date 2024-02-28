package com.codersgate.ticketraider.domain.place.service

import com.codersgate.ticketraider.domain.place.dto.CreatePlaceRequest
import com.codersgate.ticketraider.domain.place.dto.PlaceResponse
import com.codersgate.ticketraider.domain.place.dto.UpdatePlaceRequest

interface PlaceService {

    fun createPlace(request: CreatePlaceRequest)

    fun updatePlace(placeId: Long, request: UpdatePlaceRequest)

    fun deletePlace(placeId: Long)

    fun getPlaceList(): List<PlaceResponse>

    fun getPlace(placeId: Long): PlaceResponse
}