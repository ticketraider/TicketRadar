package com.codersgate.ticketraider.domain.event.dto

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.price.Price
import java.text.SimpleDateFormat
import java.util.Date

data class EventResponse(
    val id : Long,
    var title : String,
    var likeCount : Int,
    var startDate : Date,
    var endDate : Date,
    var eventInfo : String,
    var averageRating : Float,
    var posterImage : String,
    val price: Price
){
    companion object {
        fun from(event : Event) : EventResponse{
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
                price = event.price
            )
        }
    }
}

