package com.woowacourse.friendogly.domain.model

import kotlinx.datetime.LocalDateTime

data class Footprint(
    val footprintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime,
    val isMine: Boolean,
    val imageUrl: String?,
)
