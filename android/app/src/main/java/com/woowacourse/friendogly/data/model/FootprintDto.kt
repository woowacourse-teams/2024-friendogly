package com.woowacourse.friendogly.data.model

data class FootprintDto(
    val footprintId: Int,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
)
