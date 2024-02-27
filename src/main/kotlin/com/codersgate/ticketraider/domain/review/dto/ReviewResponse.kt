package com.codersgate.ticketraider.domain.review.dto

import com.codersgate.ticketraider.domain.review.model.Review

data class ReviewResponse(
    val title : String,

    val content : String,

    val rating : Int,

//    val userId : Long,
//
//    val eventId : Long,
)

fun Review.toResponse() : ReviewResponse{
    return ReviewResponse(
        title = this.title,
        content = this.content,
        rating = this.rating,
//        userId = this.userId,
//        eventId = this.eventId,
    )
}