package com.happy.friendogly.remote.model.error

import kotlinx.serialization.json.Json

fun createErrorResponse(responseBodyString: String): ErrorResponse? =
    try {
        Json.decodeFromString<ErrorResponse>(responseBodyString)
    } catch (e: Exception) {
        null
    }

fun createErrorException(
    url: String?,
    httpCode: Int,
    errorResponse: ErrorResponse?,
): Exception? {
    return when (httpCode) {
        400 -> handleBadRequestError(errorResponse?.data, url)
        401 -> handleUnauthorizedError(errorResponse?.data, url)
        403 -> handleForbiddenError(errorResponse?.data, url)
        404 -> handleNotFoundError(errorResponse?.data, url)
        500 -> handleInternalServerError(errorResponse?.data, url)
        else -> null
    }
}

private fun handleBadRequestError(
    data: ErrorData?,
    url: String?,
): Exception =
    when (data?.errorCode) {
        else -> BadRequestException(Throwable(data?.errorMessage), url)
    }

fun handleUnauthorizedError(
    data: ErrorData?,
    url: String?,
): Exception =
    when (data?.errorCode) {
        else -> InvalidAccessTokenExpire(Throwable(data?.errorMessage), url)
    }

fun handleForbiddenError(
    data: ErrorData?,
    url: String?,
): Exception =
    when (data?.errorCode) {
        else -> InvalidAccessTokenException(Throwable(data?.errorMessage), url)
    }

fun handleNotFoundError(
    data: ErrorData?,
    url: String?,
): Exception =
    when (data?.errorCode) {
        else -> ServerNotFoundException(Throwable(data?.errorMessage), url)
    }

fun handleInternalServerError(
    data: ErrorData?,
    url: String?,
): Exception =
    when (data?.errorCode) {
        else -> InternalServerErrorException(Throwable(data?.errorMessage), url)
    }
