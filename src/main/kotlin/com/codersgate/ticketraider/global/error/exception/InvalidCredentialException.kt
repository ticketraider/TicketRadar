package com.codersgate.ticketraider.global.error.exception

data class InvalidCredentialException(val msg: String): RuntimeException(
    "유저 정보가 일치하지 않습니다."
)