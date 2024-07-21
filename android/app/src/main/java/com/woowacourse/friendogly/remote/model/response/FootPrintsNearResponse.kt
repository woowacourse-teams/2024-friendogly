package com.woowacourse.friendogly.remote.model.response

data class FootPrintsNearResponse(
    val footPrintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
)
