package com.happy.friendogly.remote.error

import java.io.IOException

sealed class ApiExceptionResponse(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : IOException(message, cause) {
    data class BadRequest(
        override val message: String?,
        val error: ErrorResponse? = null,
    ) : ApiExceptionResponse(message)

    data class Unauthorized(
        override val message: String?,
        val error: ErrorResponse? = null,
    ) : ApiExceptionResponse(message)

    data class Forbidden(
        override val message: String?,
        val error: ErrorResponse? = null,
    ) : ApiExceptionResponse(message)

    data class NotFound(
        override val message: String?,
        val error: ErrorResponse? = null,
    ) : ApiExceptionResponse(message)

    data class ServerError(
        override val message: String?,
        val error: ErrorResponse? = null,
    ) : ApiExceptionResponse(message)
}
