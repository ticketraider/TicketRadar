package com.codersgate.ticketraider.domain.event.repository


import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.QEvent
import com.codersgate.ticketraider.domain.event.model.price.QPrice
import com.codersgate.ticketraider.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class EventRepositoryImpl : QueryDslSupport(), CustomEventRepository {


    private val event = QEvent.event
    private val price = QPrice.price
    override fun findByPageable(pageable: Pageable, categoryId: Long?, status: String?): Page<Event> {
        val totalCount = queryFactory.select(event.count()).from(event).fetchOne() ?: 0L
        val builder = BooleanBuilder()
        if (categoryId != null) {
            builder.and(event.category.id.eq(categoryId))
        }
        val contents = queryFactory.selectFrom(event)
            .leftJoin(event.price, price)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .where(builder)
            .orderBy(*getOrderSpecifier(pageable, event, status))
            .fetch()
        return PageImpl(contents, pageable, totalCount)
    }

    private fun getOrderSpecifier(
        pageable: Pageable,
        path: EntityPathBase<*>,
        sortDir: String?
    ): Array<OrderSpecifier<*>> {
        val sortField = setOf("id", "title", "created_at", "average_rating", "like_count", "review_count")
        val pathBuilder = PathBuilder(path.type, path.metadata)
        return pageable.sort.toList().map { order ->

            if (order.property in sortField) {
                OrderSpecifier(
                    if (sortDir == "ASC") Order.ASC
                    else Order.DESC, pathBuilder.get(order.property) as Expression<Comparable<*>>
                )
            } else {
                OrderSpecifier(Order.DESC, pathBuilder.get("id") as Expression<Comparable<*>>)
            }
        }.toTypedArray()
    }

    override fun findByPageableAndCount(pageable: Pageable, keyword : String?): Page<Event?> {
        val orderSpecifiers = mutableListOf<OrderSpecifier<*>>()
        val totalCountQuery = queryFactory.select(event.count()).from(event)
        val totalCount = totalCountQuery.fetchOne() ?: 0L
        pageable.sort.forEach {
            val orderSpecifier = when (it.property) {
                "likecount" -> event.likeCount.desc()
                "reviewcount" -> event.reviewCount.desc()
                "title" -> event.title.asc()
                "startDate" -> event.startDate.asc()
                "average_rating" -> event.averageRating.desc()
                "place" -> event.place.name.asc()
                else -> event.startDate.asc()
            }
            orderSpecifiers.add(orderSpecifier)
        }

        if(!keyword.isNullOrBlank()){
            orderSpecifiers.add(
                CaseBuilder()
                    .`when`(event.place.address.contains(keyword)).then(1)
                    .otherwise(2)
                    .asc()
            )
        }
        val query = queryFactory.selectFrom(event)
            .orderBy(*orderSpecifiers.toTypedArray())
        val events = query.fetch()
        return PageImpl(events, pageable, totalCount)
    }
}