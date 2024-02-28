package com.codersgate.ticketraider.domain.category.controller


import com.codersgate.ticketraider.domain.category.dto.CategoryResponse
import com.codersgate.ticketraider.domain.category.dto.CreateCategoryRequest
import com.codersgate.ticketraider.domain.category.dto.UpdateCategoryRequest
import com.codersgate.ticketraider.domain.category.service.CategoryService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @Operation(summary = "카테고리 생성")
    @PostMapping
    //@PreAuthorize("hasAnyRole('ADMIN')")
    fun createCategory(
        @Valid @RequestBody request: CreateCategoryRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(categoryService.createCategory(request))
    }

    @Operation(summary = "카테고리 수정")
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun updateCategory(
        @PathVariable categoryId: Long,
        @Valid @RequestBody request: UpdateCategoryRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(categoryService.updateCategory(categoryId, request))
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun deleteCategory(
        @PathVariable categoryId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(categoryService.deleteCategory(categoryId))
    }

    @Operation(summary = "카테고리 목록 조회")
    @GetMapping
    fun getCategoryList(
        @PageableDefault(
            size = 15,
            sort = ["id"]
        ) pageable: Pageable,
        @RequestParam(
            value = "status",
            required = false
        ) status: String?
    ): ResponseEntity<Page<CategoryResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(categoryService.getPaginatedCategoryList(pageable, status))
    }

    @Operation(summary = "카테고리 조회")
    @GetMapping("/{categoryId}")
    fun getCategory(
        @PathVariable categoryId: Long
    ): ResponseEntity<CategoryResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(categoryService.getCategory(categoryId))
    }
}
