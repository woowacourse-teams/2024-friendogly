package com.happy.friendogly.data.error

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class ErrorCodeDto {
    @SerializedName("DEFAULT_ERROR_CODE")
    DEFAULT_ERROR_CODE,
}
