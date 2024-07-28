package com.happy.friendogly.domain.error

data class ErrorData(
    val errorCode: String,
    val errorMessage: String,
    val detail: List<String>,
)
