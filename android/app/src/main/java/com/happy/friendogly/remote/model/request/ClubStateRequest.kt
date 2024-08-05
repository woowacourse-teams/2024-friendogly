package com.happy.friendogly.remote.model.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class ClubStateRequest {
    @SerializedName("OPEN")
    OPEN,

    @SerializedName("CLOSE")
    CLOSE,
}
