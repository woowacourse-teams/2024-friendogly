package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val isSuccess: Boolean,
    val data: T,
)
