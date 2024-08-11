package com.happy.friendogly.data.error

data class ApiExceptionDto(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val httCode: Int,
    val error: ErrorDto,
) : Exception(message, cause)
