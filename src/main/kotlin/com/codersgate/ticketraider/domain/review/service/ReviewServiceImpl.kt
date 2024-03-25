package com.codersgate.ticketraider.domain.review.service

import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.review.dto.CreateReviewRequest
import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.dto.UpdateReviewRequest
import com.codersgate.ticketraider.domain.review.model.Review
import com.codersgate.ticketraider.domain.review.repository.ReviewRepository
import com.codersgate.ticketraider.domain.ticket.entity.TicketStatus
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository,
) : ReviewService{

    override fun createReview(memberId: Long, request: CreateReviewRequest) {

        val member = memberRepository.findByIdOrNull(memberId)
            ?:throw NotFoundException()

        // 티켓 내역 확인
        val ticket =  member.tickets.find{
            it.event.id == request.eventId
//                    && it.ticketStatus == TicketStatus.EXPIRED
        }?: throw IllegalStateException("Expired ticket not found for event id: ${request.eventId}")

        val review = reviewRepository.save(
            Review(
                request.title,
                request.content,
                request.rating,
                member,
                ticket.event,
            )
        )

        review.event.reviewCount++
    }

    override fun getReviewList_V2(pageable: Pageable, memberId : Long?, eventId : Long?) : Page<ReviewResponse>
    {
        return reviewRepository.getReviewList_V2(pageable, memberId, eventId).map{ ReviewResponse.from(it) }
    }

    override fun getReviewList(pageable : Pageable): Page<ReviewResponse> {
        return reviewRepository.getReviewList(pageable).map{ ReviewResponse.from(it) }
    }

    override fun getReviewListByEvent(pageable : Pageable, eventId: Long): Page<ReviewResponse> {
        return reviewRepository.getReviewListByEventId(pageable, eventId).map{ ReviewResponse.from(it) }
    }

    override fun getReviewListByUser(pageable : Pageable, memberId: Long): Page<ReviewResponse> {
        return reviewRepository.getReviewListByUserId(pageable, memberId).map{ ReviewResponse.from(it) }
    }

    override fun getReview(reviewId: Long): ReviewResponse {
        return ReviewResponse.from(
            reviewRepository.findByIdOrNull(reviewId)
            ?:throw NotFoundException()
        )
    }

    override fun updateReview(reviewId: Long, request: UpdateReviewRequest) {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw NotFoundException()
        review.title = request.title
        review.content = request.content
        review.rating = request.rating

        reviewRepository.save(review)
    }

    override fun deleteReview(reviewId: Long) {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw NotFoundException()
        review.event.reviewCount--
        reviewRepository.delete(review)
    }
}