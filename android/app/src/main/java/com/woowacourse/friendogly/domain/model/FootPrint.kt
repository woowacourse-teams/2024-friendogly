package com.woowacourse.friendogly.domain.model

data class FootPrint(
    val footPrintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val isMine: Boolean,
)
