package com.codersgate.ticketraider.domain.event.repository


import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.QEvent
import com.codersgate.ticketraider.domain.event.model.price.QPrice
import com.codersgate.ticketraider.global.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class EventRepositoryImpl : QueryDslSupport(), CustomEventRepository {


    private val event = QEvent.event
    private val price = QPrice.price
    override fun findByPageable(
        pageable: Pageable,
        sortStatus: String?,
        searchStatus: String?,
        category: String?,
        keyword: String?
    ): Page<Event> {

        val totalCount = queryFactory.select(event.count()).from(event).fetchOne() ?: 0L
        val builder = BooleanBuilder()

        if (category != null) {
            builder.and(event.category.title.eq(category))
        }
        if (searchStatus != null) {
            if (searchStatus == "title") {
                builder.and(event.title.like("%$keyword%"))
            }
            if (searchStatus == "location") {
                builder.and(event.place.address.like("%$keyword%"))
            }
        }



        val query = queryFactory.selectFrom(event)
            .leftJoin(event.price, price)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .where(builder)
//            .orderBy(*getOrderSpecifier(pageable, event, sortStatus))
//            .fetch()

        if (sortStatus == "likes") {
            query.orderBy(event.likeCount.desc())
        }
        if (sortStatus == "reviews") {
            query.orderBy(event.reviewCount.desc())
        }
        if (sortStatus == "rating") {
            query.orderBy(event.averageRating.desc())
        }
        else {
            query.orderBy(event.averageRating.desc())
        }


        val contents = query.fetch()
        return PageImpl(contents, pageable, totalCount)
    }

//    private fun getOrderSpecifier(
//        pageable: Pageable,
//        path: EntityPathBase<*>,
//        sortDir: String?
//    ): Array<OrderSpecifier<*>> {
//
//        val pathBuilder = PathBuilder(path.type, path.metadata)
//        return pageable.sort.toList().map { order ->
//
//
//            OrderSpecifier(
//                if (sortDir == "좋아요 순") {
//                    Order.ASC
//                }
//                else Order.DESC, pathBuilder.get(order.property) as Expression<Comparable<*>>
//            )
//
//        }.toTypedArray()
//    }

    override fun findByPageableAndCount(pageable: Pageable): Page<Event?> {
        val builder = BooleanBuilder()
        val totalCount = queryFactory.select(event.count()).from(event).where(builder).fetchOne() ?: 0L
        val query = queryFactory.selectFrom(event)
            .where(builder)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        if (pageable.sort.isSorted) {
            when (pageable.sort.first()?.property) {
                "likecount" -> query.orderBy(event.likeCount.asc())
                "reviewcount" -> query.orderBy(event.reviewCount.asc())
                else -> query.orderBy(event.createdAt.asc())
            }
        } else {
            query.orderBy(event.createdAt.asc())
        }
        val events = query.fetch()
        return PageImpl(events, pageable, totalCount)
    }
}