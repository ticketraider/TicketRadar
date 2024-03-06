package com.codersgate.ticketraider.domain.review.repository

import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomReviewRepository {
    fun getReviewList(pageable: Pageable) : Page<Review>
    fun getReviewList_V2(pageable: Pageable, memberId : Long?,eventId : Long?) : Page<Review>
    fun getReviewListByUserId(pageable: Pageable, memberId : Long) : Page<Review>
    fun getReviewListByEventId(pageable: Pageable, eventId : Long): Page<Review>
}