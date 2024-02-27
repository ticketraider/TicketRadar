package com.codersgate.ticketraider.error.exception.dto

import com.codersgate.ticketraider.error.exception.status.ResultCode


data class BaseResponse<T>(
    val resultCode: String = ResultCode.SUCCESS.name,
    val date: T? = null,
    val message: String = ResultCode.SUCCESS.msg
)
