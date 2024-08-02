package com.happy.friendogly.data.error

data class ErrorDataDto(
    val errorCode: String,
    val errorMessage: String,
    val detail: List<String>,
)
