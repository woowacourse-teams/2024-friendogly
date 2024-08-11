package com.happy.friendogly.remote.error

data class ApiExceptionResponse(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val httpCode: Int,
    val error: ErrorResponse,
) : Exception(message, cause)
