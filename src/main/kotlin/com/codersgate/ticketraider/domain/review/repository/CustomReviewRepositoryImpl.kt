package com.codersgate.ticketraider.domain.review.repository

import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.model.QReview
import com.codersgate.ticketraider.domain.review.model.Review
import com.codersgate.ticketraider.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class CustomReviewRepositoryImpl : CustomReviewRepository, QueryDslSupport(){
    private var q_review = QReview.review

    override fun getReviewList_V2(pageable: Pageable, memberId: Long?, eventId: Long?): Page<Review> {

        val whereClause = BooleanBuilder()

        memberId?.let{whereClause.and(q_review.member.id.eq(memberId))}
        eventId?.let{whereClause.and(q_review.event.id.eq(eventId))}

        val totalCounts = queryFactory
            .select(q_review.count())
            .from(q_review)
            .where(whereClause)
            .fetchOne()
            ?:0L

        val contents = queryFactory.selectFrom(q_review)
            .where(whereClause)
            .orderBy(q_review.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return PageImpl(contents, pageable, totalCounts)

    }
    override fun getReviewList(pageable: Pageable): Page<Review> {
        val totalCounts = queryFactory
            .select(q_review.count())
            .from(q_review)
            .fetchOne()
            ?:0L

        val contents = queryFactory.selectFrom(q_review)
            .orderBy(q_review.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return PageImpl(contents, pageable, totalCounts)

    }

    override fun getReviewListByUserId(pageable: Pageable, memberId:Long): Page<Review> {

        val whereClause = BooleanBuilder()

        whereClause.and(q_review.member.id.eq(memberId))

        val totalCounts = queryFactory
            .select(q_review.count())
            .where(whereClause)
            .from(q_review)
            .fetchOne()
            ?:0L

        val contents = queryFactory.selectFrom(q_review)
            .orderBy(q_review.createdAt.desc())
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return PageImpl(contents, pageable, totalCounts)
    }

    override fun getReviewListByEventId(pageable: Pageable, eventId: Long): Page<Review> {

        val whereClause = BooleanBuilder()

        whereClause.and(q_review.event.id.eq(eventId))

        val totalCounts = queryFactory
            .select(q_review.count())
            .where(whereClause)
            .from(q_review)
            .fetchOne()
            ?:0L

        val contents = queryFactory.selectFrom(q_review)
            .orderBy(q_review.createdAt.desc())
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return PageImpl(contents, pageable, totalCounts)
    }
}