package com.happy.friendogly.domain.mapper

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.error.ErrorDataDto
import com.happy.friendogly.data.error.ErrorDto
import com.happy.friendogly.domain.error.ApiException
import com.happy.friendogly.domain.error.Error
import com.happy.friendogly.domain.error.ErrorData

fun ApiExceptionDto.toDomain(): ApiException =
    when (this) {
        is ApiExceptionDto.Unauthorized -> {
            ApiException.Unauthorized(message = message, error = this.error?.toDomain())
        }

        is ApiExceptionDto.Forbidden -> {
            ApiException.Forbidden(message = message, error = this.error?.toDomain())
        }

        is ApiExceptionDto.BadRequest -> {
            ApiException.BadRequest(message = message, error = this.error?.toDomain())
        }

        is ApiExceptionDto.NotFound -> {
            ApiException.NotFound(message = message, error = this.error?.toDomain())
        }

        is ApiExceptionDto.ServerError -> {
            ApiException.ServerError(message = message, error = this.error?.toDomain())
        }
    }

fun ErrorDto.toDomain(): Error {
    return Error(
        isSuccess = this.isSuccess,
        data = this.data.toDomain(),
    )
}

fun ErrorDataDto.toDomain(): ErrorData {
    return ErrorData(
        errorCode = errorCode,
        errorMessage = errorMessage,
        detail = detail,
    )
}
