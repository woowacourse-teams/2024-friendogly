package com.woowacourse.friendogly.data.model

data class FootPrintDto(
    val footPrintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
)