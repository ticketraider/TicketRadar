package com.codersgate.ticketraider.domain.event.dto

import com.codersgate.ticketraider.domain.event.dto.price.PriceResponse
import com.codersgate.ticketraider.domain.event.dto.seat.AvailableSeatResponse
import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.price.Price
import com.codersgate.ticketraider.domain.event.model.seat.AvailableSeat
import com.codersgate.ticketraider.domain.place.dto.PlaceResponse
import com.codersgate.ticketraider.domain.place.model.Place
import java.time.LocalDate

data class EventResponse(
    val id: Long,
    val title: String,
    val likeCount: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val eventInfo: String,
    val averageRating: Float,
    val posterImage: String,
    val place: String,
    val price: PriceResponse,
    val reviewCount : Int,
) {
    companion object {
        fun from(event: Event): EventResponse {
            return EventResponse(
                id = event.id!!,
                title = event.title,
                likeCount = event.likeCount,
                startDate = event.startDate,
                endDate = event.endDate,
                eventInfo = event.eventInfo,
                averageRating = event.averageRating,
                posterImage = event.posterImage,
                place = event.place.name,
                reviewCount = event.reviewCount,
                price = PriceResponse.from(event.price!!)
            )
        }
    }
}

