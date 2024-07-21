package com.woowacourse.friendogly.remote.model.response

data class BaseResponse<T>(
    val data: T,
    val errorCode: String,
)
