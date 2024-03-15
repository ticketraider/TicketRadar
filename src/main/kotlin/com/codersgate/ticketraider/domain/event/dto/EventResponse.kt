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

        fun mapToEventResponse(value: LinkedHashMap<*, *>?): EventResponse? {
            value ?: return null // 값이 null이면 null 반환

            val id = (value["id"] as? Int)?.toLong() ?: return null
            val title = value["title"] as? String ?: ""
            val likeCount = (value["likeCount"] as? Int) ?: 0
            val startDate = LocalDate.parse(value["startDate"] as? String ?: "") // LocalDate 형식에 맞게 파싱 필요
            val endDate = LocalDate.parse(value["endDate"] as? String ?: "") // LocalDate 형식에 맞게 파싱 필요
            val eventInfo = value["eventInfo"] as? String ?: ""
            val averageRating = (value["averageRating"] as? Double)?.toFloat() ?: 0.0f
            val posterImage = value["posterImage"] as? String ?: ""
            val place = value["place"] as? String ?: ""

            val priceValue = value["price"] as? LinkedHashMap<*, *>
            val price = if (priceValue != null) {
                val seatRPrice = (priceValue["seatRPrice"] as? Int) ?: 0
                val seatSPrice = (priceValue["seatSPrice"] as? Int) ?: 0
                val seatAPrice = (priceValue["seatAPrice"] as? Int) ?: 0
                PriceResponse(seatRPrice, seatSPrice, seatAPrice)
            } else {
                PriceResponse(0, 0, 0) // 기본값 설정
            }

            val reviewCount = (value["reviewCount"] as? Int) ?: 0

            return EventResponse(
                id,
                title,
                likeCount,
                startDate,
                endDate,
                eventInfo,
                averageRating,
                posterImage,
                place,
                price,
                reviewCount
            )
        }
    }
}