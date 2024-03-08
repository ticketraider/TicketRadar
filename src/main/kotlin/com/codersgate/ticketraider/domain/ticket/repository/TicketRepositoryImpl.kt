package com.codersgate.ticketraider.domain.ticket.repository

import com.codersgate.ticketraider.domain.ticket.entity.QTicket
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDate

class TicketRepositoryImpl : QueryDslSupport(), CustomTicketRepository {
    private val ticket = QTicket.ticket
    override fun findAllByPlaceId(placeId: Long): List<Ticket?> {
        val contents = queryFactory.select(ticket)
            .from(ticket)
            .where(ticket.event.place.id.eq(placeId))
            .orderBy(ticket.date.desc())
            .fetch()
        return contents
    }

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

    override fun chkTicket(eventId:Long, date: LocalDate, grade: TicketGrade, seatNo:Int): Ticket? {
        val whereClause = BooleanBuilder()
        whereClause.and(ticket.event.id.eq(eventId))
        whereClause.and(ticket.date.eq(date ))
        whereClause.and(ticket.grade.eq(grade))
        whereClause.and(ticket.seatNo.eq(seatNo))


        return queryFactory.select(ticket)
            .from(ticket)
            .where(whereClause)
            .orderBy(ticket.date.desc())
            .fetchOne()
    }
}