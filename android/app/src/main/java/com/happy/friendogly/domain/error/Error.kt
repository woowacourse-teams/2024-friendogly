package com.happy.friendogly.domain.error

data class Error(
    val isSuccess: Boolean,
    val data: ErrorData,
)
