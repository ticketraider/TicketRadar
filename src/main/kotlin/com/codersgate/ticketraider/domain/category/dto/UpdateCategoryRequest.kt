package com.codersgate.ticketraider.domain.category.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateCategoryRequest(
    @field: NotBlank
    @field: Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    @JsonProperty("title")
    val title: String
)