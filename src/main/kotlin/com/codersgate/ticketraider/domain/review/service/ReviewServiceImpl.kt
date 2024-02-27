package com.codersgate.ticketraider.domain.review.service

import com.codersgate.ticketraider.domain.review.dto.CreateReviewRequest
import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.model.Review
import com.codersgate.ticketraider.domain.review.repository.ReviewRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull

class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
) : ReviewService{

    override fun createReview(request: CreateReviewRequest) {
        reviewRepository.save(
            Review(
            request.title,
            request.content,
            request.rating,
//            request.userId,
//            request.eventId
            )
        )
    }

    override fun getReviewList_V2(pageable: Pageable, userId : Long, eventId : Long) : Page<ReviewResponse>
    {
        return reviewRepository.getReviewList_V2(pageable, userId, eventId).map{ ReviewResponse.from(it) }
    }
    override fun getReviewList(pageable : Pageable): Page<ReviewResponse> {
        return reviewRepository.getReviewList(pageable).map{ ReviewResponse.from(it) }
    }

    override fun getReviewListByEvent(pageable : Pageable, eventId: Long): Page<ReviewResponse> {
        return reviewRepository.getReviewListByEventId(pageable, eventId).map{ ReviewResponse.from(it) }
    }

    override fun getReviewListByUser(pageable : Pageable, userId: Long): Page<ReviewResponse> {
        return reviewRepository.getReviewListByUserId(pageable, userId).map{ ReviewResponse.from(it) }
    }

    override fun getReview(reviewId: Long): ReviewResponse {
        return ReviewResponse.from(
            reviewRepository.findByIdOrNull(reviewId)
            ?:throw NotFoundException()
        )
    }

    override fun updateReview(reviewId: Long) {
        TODO("Not yet implemented")
    }

    override fun deleteReview(reviewId: Long) {
        TODO("Not yet implemented")
    }
}