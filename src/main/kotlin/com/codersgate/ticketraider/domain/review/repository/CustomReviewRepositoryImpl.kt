package com.codersgate.ticketraider.domain.review.repository

import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.model.QReview
import com.codersgate.ticketraider.global.infra.querydsl.QueryDslSupport
import com.sun.java.accessibility.util.EventID
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class CustomReviewRepositoryImpl : CustomReviewRepository, QueryDslSupport(){
    private var q_review = QReview.review
    override fun getReviewList(pageable: Pageable): Page<ReviewResponse> {
         queryFactory.selectFrom(q_review)
            .fetchAll()

       // return PageImpl(totalCounts, pageable, contents)
        TODO()
    }

    override fun getReviewListByUserId(pageable: Pageable, userId:Long): Page<ReviewResponse> {
        TODO("Not yet implemented")
    }

    override fun getReviewListEventId(pageable: Pageable, eventId: Long): Page<ReviewResponse> {
        TODO("Not yet implemented")
    }
}