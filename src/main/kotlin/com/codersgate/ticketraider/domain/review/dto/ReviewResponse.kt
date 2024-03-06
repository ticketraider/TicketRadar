package com.codersgate.ticketraider.domain.review.dto

import com.codersgate.ticketraider.domain.review.model.Review

data class ReviewResponse(
    val title: String,

    val content: String,

    val rating: Int,

    val userId : Long,  //userId 보단 memberId로 바꾸는 게 좋을것 같음

    val eventId : Long,

) {
    companion object {
        fun from(review: Review): ReviewResponse {
            return ReviewResponse(
                title = review.title,
                content = review.content,
                rating = review.rating,
                userId = review.member.id!!,
                eventId = review.event.id!!,
            )
        }
    }
}