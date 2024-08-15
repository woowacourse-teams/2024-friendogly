package com.happy.friendogly.remote.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val isSuccess: Boolean,
    val data: ErrorDataResponse,
)
