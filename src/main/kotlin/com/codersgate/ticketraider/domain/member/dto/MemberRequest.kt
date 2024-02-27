package com.codersgate.ticketraider.domain.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class MemberRequest(
    @field: Email(message = "Incorrect E-mail form")
    val email: String,
    @field: Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
    val password: String,
    @field: Size(min = 2, max = 10, message = "Nickname must be between 2 and 10 characters")
    val nickname: String
)