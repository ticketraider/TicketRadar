package com.codersgate.ticketraider.domain.ticket.repository

import com.codersgate.ticketraider.domain.ticket.entity.QTicket
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class TicketRepositoryImpl : QueryDslSupport(), CustomTicketRepository {
    private val ticket = QTicket.ticket

    override fun getListByUserId(pageable: Pageable, userId: Long): Page<Ticket> {

        val whereClause = BooleanBuilder()
        whereClause.and(ticket.member.id.eq(userId))
        val totalCounts = queryFactory
            .select(ticket.count())
            .where(whereClause)
            .from(ticket)
            .fetchOne() ?: 0L

        val contents = queryFactory.select(ticket)
            .from(ticket)
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(ticket.date.desc())
            .fetch()

        return PageImpl(contents, pageable, totalCounts)
    }
}