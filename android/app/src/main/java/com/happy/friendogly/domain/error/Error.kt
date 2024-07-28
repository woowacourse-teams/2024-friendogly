package com.happy.friendogly.domain.error

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val isSuccess: Boolean,
    val data: ErrorData,
)
