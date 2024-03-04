package com.codersgate.ticketraider.domain.event.repository

import com.codersgate.ticketraider.domain.category.model.Category
import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.QEvent
import com.codersgate.ticketraider.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class EventRepositoryImpl : QueryDslSupport(), CustomEventRepository {

    private val event = QEvent.event
    override fun findByPageable(pageable: Pageable, category: Category?): Page<Event> {

        val totalCount = queryFactory.select(event.count()).from(event).fetchOne() ?: 0L

        val builder = BooleanBuilder()
        check(category != null){
            builder.and(event.category.eq(category))
        }

        val contents = queryFactory.select(event)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .where(builder)
            .orderBy(*getOrderSpecifier(pageable, event))
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }

    private fun getOrderSpecifier(pageable: Pageable, path: EntityPathBase<*>): Array<OrderSpecifier<*>> {
        val pathBuilder = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map { order ->
            OrderSpecifier(
                if (order.isAscending) Order.ASC else Order.DESC,
                pathBuilder.get(order.property) as Expression<Comparable<*>>
            )
        }.toTypedArray()
    }

}