package com.woowacourse.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class FootprintsNearResponse(
    val footprintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
    val imageUrl: String?,
)
