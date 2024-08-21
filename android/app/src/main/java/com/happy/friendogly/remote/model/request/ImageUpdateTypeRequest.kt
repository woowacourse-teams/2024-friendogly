package com.happy.friendogly.remote.model.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class ImageUpdateTypeRequest {
    @SerializedName("UPDATE")
    UPDATE,

    @SerializedName("NOT_UPDATE")
    NOT_UPDATE,

    @SerializedName("DELETE")
    DELETE,
}
