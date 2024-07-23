package com.woowacourse.friendogly.remote.model.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class FootprintSaveResponse(
    @SerializedName("id") val footprintId: Long,
    val latitude: Double,
    val longitude: Double,
)
