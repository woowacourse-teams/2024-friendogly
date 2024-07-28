package com.happy.friendogly.domain.error

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class ErrorCode {
    @SerializedName("DEFAULT_ERROR_CODE")
    DEFAULT_ERROR_CODE,
}
