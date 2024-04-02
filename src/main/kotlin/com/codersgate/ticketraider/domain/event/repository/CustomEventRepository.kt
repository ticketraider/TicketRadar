package com.codersgate.ticketraider.domain.event.repository

import com.codersgate.ticketraider.domain.event.model.Event
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomEventRepository {

    fun findByPageable(
        pageable: Pageable,
        sortStatus: String?,
        searchStatus: String?,
        category: String?,
        keyword: String?
    ): Page<Event>
}