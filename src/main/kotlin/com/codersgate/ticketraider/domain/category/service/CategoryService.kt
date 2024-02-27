package com.codersgate.ticketraider.domain.category.service

import com.codersgate.ticketraider.domain.category.dto.CategoryResponse
import com.codersgate.ticketraider.domain.category.dto.CreateCategoryRequest
import com.codersgate.ticketraider.domain.category.dto.UpdateCategoryRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CategoryService {

    fun createCategory(request: CreateCategoryRequest)

    fun updateCategory(categoryId: Long, request: UpdateCategoryRequest)

    fun deleteCategory(categoryId: Long)

    fun getCategoryList(): List<CategoryResponse>

    fun getCategory(categoryId: Long): CategoryResponse

    fun getPaginatedCategoryList(pageable: Pageable, status: String?): Page<CategoryResponse>?
}