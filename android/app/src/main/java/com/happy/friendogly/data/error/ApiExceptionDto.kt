package com.happy.friendogly.data.error

import java.io.IOException

data class ApiExceptionDto(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val httpCode: Int,
    val error: ErrorDto,
) : IOException(message, cause) {
    companion object {
        // TODO 임시 이미지 사이즈 에러
        val FileSizeExceedExceptionDto =
            ApiExceptionDto(
                httpCode = 400,
                error =
                    ErrorDto(
                        isSuccess = false,
                        data =
                            ErrorDataDto(
                                errorCode = ErrorCodeDto.FILE_SIZE_EXCEED,
                                errorMessage = "",
                                detail = emptyList(),
                            ),
                    ),
            )
    }
}
