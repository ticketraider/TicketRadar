package com.codersgate.ticketraider.domain.event.dto

import com.codersgate.ticketraider.domain.category.model.Category
import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.price.Price
import com.codersgate.ticketraider.domain.event.model.seat.AvailableSeat
import com.codersgate.ticketraider.domain.place.model.Place
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class EventRequest(

    val categoryId: Long,

    val posterImage: String,

    val title: String,

    val eventInfo: String,

    @field: Pattern(
        regexp = dateRegexp,
        message = "Please match date format yyyy-MM-dd HH:mm:ss"
    )
    @Schema(pattern = dateRegexp)
    @JsonProperty("startDate")
    private val _startDate: String,

    @field: Pattern(
        regexp = dateRegexp,
        message = "Please match date format yyyy-MM-dd HH:mm:ss"
    )
    @Schema(pattern = dateRegexp)
    @JsonProperty("endDate")
    private val _endDate: String,

    val place: String,

    val seatRPrice: Int,

    val seatSPrice: Int,

    val seatAPrice: Int,

    ) {

    @JsonIgnore
    val startDate: LocalDate = convertStringToLocalDateTime(this._startDate)

    @JsonIgnore
    val endDate: LocalDate = convertStringToLocalDateTime(this._endDate)

    private fun convertStringToLocalDateTime(date: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(date, formatter)
    }

    companion object {
        const val dateRegexp =
            "^2[0-9]{3}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$"
    }

    fun toPriceAndEvent(category: Category, place: Place): Pair<Price, Event> {

        val event = Event(
            posterImage = posterImage,
            title = title,
            eventInfo = eventInfo,
            startDate = startDate,
            endDate = endDate,
            place = place,
            category = category,
        )
        val price = Price(
            seatRPrice = seatRPrice,
            seatSPrice = seatSPrice,
            seatAPrice = seatAPrice,
            event = event
        )
        return Pair(price, event)
    }

    fun toAvailableSeat(event: Event, place: Place, localDate: LocalDate): AvailableSeat {
        val availableSeat = AvailableSeat(
            event = event,
            date = localDate,//여기도 알맞은 날짜 넣도록하기
            seatR = place.seatR,
            seatS = place.seatS,
            seatA = place.seatA
        )
        return availableSeat
    }
}