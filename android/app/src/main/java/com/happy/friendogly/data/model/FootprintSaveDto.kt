package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDateTime

class FootprintSaveDto(
    val footprintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime,
)
