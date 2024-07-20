package com.woowacourse.friendogly.domain.model

data class Footprint(
    val footprintId: Int,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
)
