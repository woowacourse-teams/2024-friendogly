package com.happy.friendogly.remote.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDataResponse(
    val errorCode: ErrorCodeResponse,
    val errorMessage: String,
    val detail: List<String>,
)
