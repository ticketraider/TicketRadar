package com.codersgate.ticketraider.domain.event.dto

data class UpdateEventRequest(
    var posterImage : String,
    var title : String,
    var eventInfo : String,
    var startDate : String,
    var endDate : String,
    )
