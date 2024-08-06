package com.happy.friendogly.remote.model.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class ClubFilterConditionRequest {
    @SerializedName("ALL")
    ALL,

    @SerializedName("OPEN")
    OPEN,

    @SerializedName("ABLE_TO_JOIN")
    ABLE_TO_JOIN,
}
