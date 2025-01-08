package com.happy.friendogly.remote.error

import java.io.IOException

data class ApiExceptionResponse(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val httpCode: Int,
    val error: ErrorResponse,
) : IOException(message, cause)
