package com.woowacourse.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val data: T,
    val errorCode: String,
)
