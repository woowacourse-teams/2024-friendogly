package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.error.ErrorDataDto
import com.happy.friendogly.data.error.ErrorDto
import com.happy.friendogly.remote.error.ApiExceptionResponse
import com.happy.friendogly.remote.error.ErrorDataResponse
import com.happy.friendogly.remote.error.ErrorResponse

fun ApiExceptionResponse.toData(): ApiExceptionDto =
    when (this) {
        is ApiExceptionResponse.Unauthorized -> {
            ApiExceptionDto.Unauthorized(message = this.message, error = this.error?.toData())
        }

        is ApiExceptionResponse.Forbidden -> {
            ApiExceptionDto.Forbidden(message = this.message, error = this.error?.toData())
        }

        is ApiExceptionResponse.BadRequest -> {
            ApiExceptionDto.BadRequest(message = this.message, error = this.error?.toData())
        }

        is ApiExceptionResponse.NotFound -> {
            ApiExceptionDto.NotFound(message = this.message, error = this.error?.toData())
        }

        is ApiExceptionResponse.ServerError -> {
            ApiExceptionDto.ServerError(message = this.message, error = this.error?.toData())
        }
    }

fun ErrorResponse.toData(): ErrorDto {
    return ErrorDto(
        isSuccess = this.isSuccess,
        data = this.data.toData(),
    )
}

fun ErrorDataResponse.toData(): ErrorDataDto {
    return ErrorDataDto(
        errorCode = errorCode,
        errorMessage = errorMessage,
        detail = detail,
    )
}
