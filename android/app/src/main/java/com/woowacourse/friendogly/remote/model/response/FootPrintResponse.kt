package com.woowacourse.friendogly.remote.model.response

data class FootPrintResponse(
    val footPrintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
)
