package com.happy.friendogly.remote.error

import kotlinx.serialization.json.Json

fun createErrorResponse(responseBodyString: String): ErrorResponse? =
    try {
        Json.decodeFromString<ErrorResponse>(responseBodyString)
    } catch (e: Exception) {
        null
    }

fun createApiException(
    httpCode: Int,
    errorResponse: ErrorResponse,
): ApiExceptionResponse {
    return ApiExceptionResponse(
        httpCode = httpCode,
        error = errorResponse,
        message = errorResponse.data.errorMessage,
    )
}
