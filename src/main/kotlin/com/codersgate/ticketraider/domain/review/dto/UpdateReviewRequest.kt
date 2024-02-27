package com.codersgate.ticketraider.domain.review.dto

data class UpdateReviewRequest (
    val title : String,

    val content : String,

    val rating : Int,

    val userId : Long,

    var eventId : Long,
)