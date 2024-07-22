package com.woowacourse.friendogly.remote.model.response

data class FootprintsNearResponse(
    val footPrintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
    val imageUrl: String?,
)
