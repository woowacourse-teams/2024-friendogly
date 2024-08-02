package com.happy.friendogly.remote.error

import com.happy.friendogly.remote.error.ApiExceptionResponse.BadRequest
import com.happy.friendogly.remote.error.ApiExceptionResponse.Forbidden
import com.happy.friendogly.remote.error.ApiExceptionResponse.NotFound
import com.happy.friendogly.remote.error.ApiExceptionResponse.ServerError
import com.happy.friendogly.remote.error.ApiExceptionResponse.Unauthorized
import kotlinx.serialization.json.Json

fun createErrorResponse(responseBodyString: String): ErrorResponse? =
    try {
        Json.decodeFromString<ErrorResponse>(responseBodyString)
    } catch (e: Exception) {
        null
    }

fun createApiException(
    httpCode: Int,
    errorResponse: ErrorResponse?,
): ApiExceptionResponse? {
    return when (httpCode) {
        400 -> BadRequest(message = errorResponse?.data?.errorMessage, error = errorResponse)
        401 -> Unauthorized(message = errorResponse?.data?.errorMessage, error = errorResponse)
        403 -> Forbidden(message = errorResponse?.data?.errorMessage, error = errorResponse)
        404 -> NotFound(message = errorResponse?.data?.errorMessage, error = errorResponse)
        500 -> ServerError(message = errorResponse?.data?.errorMessage, error = errorResponse)
        else -> null
    }
}
