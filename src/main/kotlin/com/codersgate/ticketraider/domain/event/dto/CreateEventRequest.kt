package com.codersgate.ticketraider.domain.event.dto

data class CreateEventRequest (
    val posterImage : String,
    val title : String,
    val eventInfo : String,
)



        //최초에 적었던 리퀘스트 값
//var title : String,
//var location : String,
//var bookable : Boolean,
//var startDate : String,
//var endDate : String,
//var price : Int,
//var eventInfo : String,
//var posterImage : String,