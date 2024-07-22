package com.woowacourse.friendogly.data.model

data class FootprintDto(
    val footPrintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
    val imageUrl: String?,
)
