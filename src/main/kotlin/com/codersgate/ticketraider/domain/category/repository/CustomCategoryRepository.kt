package com.codersgate.ticketraider.domain.category.repository


import com.codersgate.ticketraider.domain.category.model.Category
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomCategoryRepository {

    fun findByPageable(pageable: Pageable): Page<Category>
}