package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.error.ErrorCodeDto
import com.happy.friendogly.data.error.ErrorDataDto
import com.happy.friendogly.data.error.ErrorDto
import com.happy.friendogly.remote.error.ApiExceptionResponse
import com.happy.friendogly.remote.error.ErrorCodeResponse
import com.happy.friendogly.remote.error.ErrorDataResponse
import com.happy.friendogly.remote.error.ErrorResponse

fun ApiExceptionResponse.toData(): ApiExceptionDto {
    return ApiExceptionDto(
        message = this.message,
        cause = this.cause,
        httCode = this.httpCode,
        error = this.error.toData(),
    )
}

fun ErrorResponse.toData(): ErrorDto {
    return ErrorDto(
        isSuccess = this.isSuccess,
        data = this.data.toData(),
    )
}

fun ErrorDataResponse.toData(): ErrorDataDto {
    return ErrorDataDto(
        errorCode = this.errorCode.toData(),
        errorMessage = this.errorMessage,
        detail = this.detail,
    )
}

fun ErrorCodeResponse.toData(): ErrorCodeDto {
    return when (this) {
        ErrorCodeResponse.DEFAULT_ERROR_CODE -> ErrorCodeDto.DEFAULT_ERROR_CODE
    }
}