package com.codersgate.ticketraider.domain.event.dto

import com.codersgate.ticketraider.domain.event.dto.price.PriceResponse
import com.codersgate.ticketraider.domain.event.dto.seat.SeatResponse
import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.price.Price
import com.codersgate.ticketraider.domain.event.model.seat.Seat
import java.text.SimpleDateFormat
import java.util.Date

data class EventResponse(
    val id : Long,
    val title : String,
    val likeCount : Int,
    val startDate : Date,
    val endDate : Date,
    val eventInfo : String,
    val averageRating : Float,
    val posterImage : String,
    val place: String,
    val price: PriceResponse,
    val seat: SeatResponse,
){
    companion object {
        fun from(event : Event, price: Price, seat: Seat) : EventResponse{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val startDate = dateFormat.parse(event.startDate)
            val endDate = dateFormat.parse(event.endDate)

            return EventResponse(
                id = event.id!!,
                title = event.title,
                likeCount = event.likeCount,
                startDate = startDate,
                endDate = endDate,
                eventInfo = event.eventInfo,
                averageRating = event.averageRating,
                posterImage = event.posterImage,
                place = event.place.name,
                price = PriceResponse.from(price),
                seat = SeatResponse.from(seat)
            )
        }
    }
}

