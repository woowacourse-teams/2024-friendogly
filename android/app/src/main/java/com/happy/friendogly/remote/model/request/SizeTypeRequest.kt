package com.happy.friendogly.remote.model.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class SizeTypeRequest {
    @SerializedName("SMALL")
    SMALL,

    @SerializedName("MEDIUM")
    MEDIUM,

    @SerializedName("LARGE")
    LARGE,
}
