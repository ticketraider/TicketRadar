package com.codersgate.ticketraider.domain.like.repository

import com.codersgate.ticketraider.domain.like.model.Like
import com.codersgate.ticketraider.domain.like.model.QLike
import com.codersgate.ticketraider.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class CustomLikeRepositoryImpl : CustomLikeRepository, QueryDslSupport() {
    
    val q_like = QLike.like
    override fun getLikeList(pageable: Pageable, memberId: Long?, eventId: Long?): Page<Like> {

        val whereClause = BooleanBuilder()

        memberId?.let{whereClause.and(q_like.member.id.eq(memberId))}
        eventId?.let{whereClause.and(q_like.event.id.eq(eventId))}

        val totalCounts = queryFactory
            .select(q_like.count())
            .from(q_like)
            .where(whereClause)
            .fetchOne()
            ?:0L

        val contents = queryFactory.selectFrom(q_like)
            .where(whereClause)
            .orderBy(q_like.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return PageImpl(contents, pageable, totalCounts)
    }
}