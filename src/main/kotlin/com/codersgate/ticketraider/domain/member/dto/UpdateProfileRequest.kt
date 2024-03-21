package com.codersgate.ticketraider.domain.member.dto

import jakarta.validation.constraints.Size

data class UpdateProfileRequest(
    @field: Size(min = 2, max = 10, message = "Nickname must be between 2 and 10 characters")
    val nickname: String,
    @field: Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
    val password: String
)
