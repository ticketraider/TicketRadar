package com.codersgate.ticketraider.domain.event.repository

import com.codersgate.ticketraider.domain.category.model.Category
import com.codersgate.ticketraider.domain.event.model.Event
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomEventRepository {

    fun findByPageable(pageable: Pageable, categoryId: Long?, status: String?): Page<Event>

    fun findByPageableAndCount(pageable: Pageable): Page<Event?>

}