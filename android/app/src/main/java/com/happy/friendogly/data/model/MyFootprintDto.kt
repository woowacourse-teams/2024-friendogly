package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDateTime

data class MyFootprintDto(
    val footprintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime,
)
