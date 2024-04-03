package com.codersgate.ticketraider.domain.review.dto

import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Range

data class UpdateReviewRequest(
    @field:Size(min = 1, max = 100)
    val title: String,

    @field:Size(min = 1, max = 1000)
    val content: String,

    @field:Range(min = 1, max = 5)
    val rating: Int,
)