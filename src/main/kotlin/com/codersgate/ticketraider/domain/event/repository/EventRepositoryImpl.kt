package com.codersgate.ticketraider.domain.event.repository

import com.codersgate.ticketraider.domain.category.model.Category
import com.codersgate.ticketraider.domain.category.repository.CategoryRepository
import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.QEvent
import com.codersgate.ticketraider.domain.event.model.price.QPrice
import com.codersgate.ticketraider.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import jakarta.persistence.OrderBy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class EventRepositoryImpl : QueryDslSupport(), CustomEventRepository {

    private val event = QEvent.event
    private val price = QPrice.price
    override fun findByPageable(pageable: Pageable, categoryId: Long?, status: String?): Page<Event> {
        val totalCount = queryFactory.select(event.count()).from(event).fetchOne() ?: 0L

        val builder = BooleanBuilder()
        if(categoryId != null){
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
    private fun getOrderSpecifier(pageable: Pageable, path: EntityPathBase<*>, sortDir : String?): Array<OrderSpecifier<*>> {
        val sortField = setOf("id", "title", "created_at", "average_rating", "likecount")

        val pathBuilder = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map { order ->

            if(order.property in sortField){
                OrderSpecifier(
                    if (sortDir == "ASC") Order.ASC
                    else Order.DESC, pathBuilder.get(order.property) as Expression<Comparable<*>>
                )
            }
            else {
                OrderSpecifier(Order.DESC, pathBuilder.get("id") as Expression<Comparable<*>>)
            }
        }.toTypedArray()
    }

//    private fun getOrderSpecifier(pageable: Pageable, path: EntityPathBase<*>, status: String?): Array<OrderSpecifier<*>> {
//        val pathBuilder = PathBuilder(path.type, path.metadata)
//
//        pageable.sort.isSorted
//
//        return pageable.sort.toList().map { order ->
//            OrderSpecifier(
//                if (status == "DESC") Order.DESC else Order.ASC,
//                pathBuilder.get(order.property) as Expression<Comparable<*>>
//            )
//        }.toTypedArray()
//    }

}