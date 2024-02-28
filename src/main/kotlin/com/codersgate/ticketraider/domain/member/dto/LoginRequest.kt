package com.codersgate.ticketraider.domain.member.dto

data class LoginRequest(
    val email: String,
    val password: String,
)
