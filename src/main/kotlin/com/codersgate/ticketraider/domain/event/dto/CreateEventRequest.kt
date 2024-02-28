package com.codersgate.ticketraider.domain.event.dto

data class CreateEventRequest (
    var posterImage : String,
    var title : String,
    var eventInfo : String,
    var startDate : String,
    var endDate : String,
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