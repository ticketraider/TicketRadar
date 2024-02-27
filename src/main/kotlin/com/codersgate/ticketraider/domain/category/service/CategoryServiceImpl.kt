package com.codersgate.ticketraider.domain.category.service

import com.codersgate.ticketraider.domain.category.dto.CategoryResponse
import com.codersgate.ticketraider.domain.category.dto.CreateCategoryRequest
import com.codersgate.ticketraider.domain.category.dto.UpdateCategoryRequest
import com.codersgate.ticketraider.domain.category.model.Category
import com.codersgate.ticketraider.domain.category.repository.CategoryRepository
import com.codersgate.ticketraider.error.exception.ModelNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {

    //생성, 수정, 삭제는 역할 ADMIN만 가능(Controller에서 역할 확인)
    override fun createCategory(request: CreateCategoryRequest) {
        val category = Category(
            title = request.title
        )
        categoryRepository.save(category)
    }

    override fun updateCategory(categoryId: Long, request: UpdateCategoryRequest) {
        val category = categoryRepository.findByIdOrNull(categoryId)
            ?: throw ModelNotFoundException("Category", categoryId)
        category.title = request.title
    }

    override fun deleteCategory(categoryId: Long) {
        val category = categoryRepository.findByIdOrNull(categoryId)
            ?: throw ModelNotFoundException("Category", categoryId)
        categoryRepository.delete(category)
    }

    //현재 getCategoryList는 getPaginatedCategoryList로 대체됨
    override fun getCategoryList(): List<CategoryResponse> {
        val categoryList = categoryRepository.findAll()
        return categoryList.map { CategoryResponse.from(it) }
    }

    override fun getCategory(categoryId: Long): CategoryResponse {
        val category = categoryRepository.findByIdOrNull(categoryId)
            ?: throw ModelNotFoundException("Category", categoryId)
        return CategoryResponse.from(category)
    }

    override fun getPaginatedCategoryList(pageable: Pageable, status: String?): Page<CategoryResponse>? {
        return categoryRepository.findByPageable(pageable).map { CategoryResponse.from(it) }
    }

}