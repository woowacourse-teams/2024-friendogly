package com.woowacourse.friendogly.remote.model.response

data class FootprintMineLatestResponse(
    val footprintId: Int,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
)
