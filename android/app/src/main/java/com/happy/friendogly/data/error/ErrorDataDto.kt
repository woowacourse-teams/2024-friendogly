package com.happy.friendogly.data.error

data class ErrorDataDto(
    val errorCode: ErrorCodeDto,
    val errorMessage: String,
    val detail: List<String>,
)
