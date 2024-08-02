package com.happy.friendogly.remote.error

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class ErrorCodeResponse {
    @SerializedName("DEFAULT_ERROR_CODE")
    DEFAULT_ERROR_CODE,
}
