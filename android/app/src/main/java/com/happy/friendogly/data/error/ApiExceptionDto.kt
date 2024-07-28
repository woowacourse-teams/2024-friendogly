package com.happy.friendogly.data.error

sealed class ApiExceptionDto(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception(message, cause) {
    data class BadRequest(
        override val message: String?,
        val error: ErrorDto? = null,
    ) : ApiExceptionDto(message)

    data class Unauthorized(
        override val message: String?,
        val error: ErrorDto? = null,
    ) : ApiExceptionDto(message)

    data class Forbidden(
        override val message: String?,
        val error: ErrorDto? = null,
    ) : ApiExceptionDto(message)

    data class NotFound(
        override val message: String?,
        val error: ErrorDto? = null,
    ) : ApiExceptionDto(message)

    data class ServerError(
        override val message: String?,
        val error: ErrorDto? = null,
    ) : ApiExceptionDto(message)
}
