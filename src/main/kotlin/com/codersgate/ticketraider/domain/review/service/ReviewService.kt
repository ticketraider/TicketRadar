package com.codersgate.ticketraider.domain.review.service

import com.codersgate.ticketraider.domain.review.dto.CreateReviewRequest
import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.dto.UpdateReviewRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface ReviewService {

    fun createReview(memberId: Long, request: CreateReviewRequest)
    fun getReviewList(pageable: Pageable): Page<ReviewResponse>
    fun getReviewList_V2(pageable: Pageable, memberId: Long?, eventId: Long?): Page<ReviewResponse>
    fun getReviewListByEvent(pageable: Pageable, eventId: Long): Page<ReviewResponse>
    fun getReviewListByUser(pageable: Pageable, memberId: Long): Page<ReviewResponse>
    fun getReview(reviewId: Long): ReviewResponse
    fun updateReview(reviewId: Long, request: UpdateReviewRequest)
    fun deleteReview(reviewId: Long)

}