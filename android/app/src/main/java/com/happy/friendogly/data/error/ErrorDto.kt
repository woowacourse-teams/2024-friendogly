package com.happy.friendogly.data.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val isSuccess: Boolean,
    val data: ErrorDataDto,
)
