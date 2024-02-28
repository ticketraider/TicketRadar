package com.codersgate.ticketraider.domain.event.dto

data class UpdateEventRequest(
    val posterImage : String,
    val title : String,
    val eventInfo : String,
    )
