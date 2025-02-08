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
        } else if (searchStatus != null) {
            if (searchStatus == "title") {
                builder.and(event.title.like("%$keyword%"))
            }
            if (searchStatus == "location") {
                builder.and(event.place.address.like("%$keyword%"))
            }
            if (searchStatus == "") {
                builder.and(event.title.like("$keyword"))
            }
        } else {
            builder.and(event.title.like("%$keyword%"))
        }


        val query = queryFactory.selectFrom(event)
            .leftJoin(event.price, price).fetchJoin()   // N+1 문제 해결.
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .where(builder)

        if (sortStatus == "likes") {
            query.orderBy(event.likeCount.desc())
        }
        if (sortStatus == "reviews") {
            query.orderBy(event.reviewCount.desc())
        }
        if (sortStatus == "rating") {
            query.orderBy(event.averageRating.desc())
        } else {
            query.orderBy(event.averageRating.desc())
        }


        val contents = query.fetch()
        return PageImpl(contents, pageable, totalCount)
    }
}