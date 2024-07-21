package com.woowacourse.friendogly.remote.model.error

import com.google.gson.annotations.SerializedName

data class ErrorResponseImpl(
    @SerializedName("success") override val success: Boolean,
    @SerializedName("status") override val status: Int,
    @SerializedName("reason") override val reason: String,
    @SerializedName("timeStamp") override val timeStamp: String,
    @SerializedName("path") override val path: String,
) : ErrorResponse
