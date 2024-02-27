package com.codersgate.ticketraider.domain.review.repository

import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomReviewRepository {
    fun getReviewList(pageable: Pageable) : Page<ReviewResponse>
    fun getReviewListByUserId(pageable: Pageable, userId : Long) : Page<ReviewResponse>
    fun getReviewListEventId(pageable: Pageable, eventId : Long): Page<ReviewResponse>
}