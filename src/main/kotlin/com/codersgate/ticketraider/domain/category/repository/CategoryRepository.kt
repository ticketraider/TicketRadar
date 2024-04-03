package com.codersgate.ticketraider.domain.category.repository

import com.codersgate.ticketraider.domain.category.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long>, CustomCategoryRepository {
    fun existsByTitle(title: String): Boolean
}