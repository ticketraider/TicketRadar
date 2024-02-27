package com.codersgate.ticketraider.domain.review.dto

import jakarta.persistence.Column

data class CreateReviewRequest(
    val title : String,

    val content : String,

    val rating : Int,

    val userId : Long,

    var eventId : Long,
)