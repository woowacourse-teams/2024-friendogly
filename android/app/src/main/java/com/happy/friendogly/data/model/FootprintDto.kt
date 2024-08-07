package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDateTime

data class FootprintDto(
    val footprintId: Long,
    val latitude: Double,
    val longitude: Double,
    val walkStatus: WalkStatusDto,
    val createdAt: LocalDateTime,
    val isMine: Boolean,
)
