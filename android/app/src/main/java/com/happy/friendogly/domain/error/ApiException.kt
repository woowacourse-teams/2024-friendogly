package com.happy.friendogly.domain.error

sealed class ApiException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception(message, cause) {
    data class BadRequest(
        override val message: String?,
        val error: Error? = null,
    ) : ApiException(message)

    data class Unauthorized(
        override val message: String?,
        val error: Error? = null,
    ) : ApiException(message)

    data class Forbidden(
        override val message: String?,
        val error: Error? = null,
    ) : ApiException(message)

    data class NotFound(
        override val message: String?,
        val error: Error? = null,
    ) : ApiException(message)

    data class ServerError(
        override val message: String?,
        val error: Error? = null,
    ) : ApiException(message)
}
