package com.codersgate.ticketraider.domain.review.service

import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.dto.toResponse
import com.codersgate.ticketraider.domain.review.repository.ReviewRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull

class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
) : ReviewService{
    override fun getReviewList(pageable : Pageable): Page<ReviewResponse> {
        TODO()
    }

    override fun getReviewListByEvent(pageable : Pageable, eventID: Long): Page<ReviewResponse> {
        TODO("Not yet implemented")
    }

    override fun getReviewListByUser(pageable : Pageable, userId: Long): Page<ReviewResponse> {
        TODO("Not yet implemented")
    }

    override fun getReview(reviewId: Long): ReviewResponse {
        return reviewRepository.findByIdOrNull(reviewId)
            ?.toResponse()
            ?:throw NotFoundException()
    }

    override fun updateReview(reviewId: Long) {
        TODO("Not yet implemented")
    }

    override fun deleteReview(reviewId: Long) {
        TODO("Not yet implemented")
    }
}