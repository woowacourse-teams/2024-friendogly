package com.happy.friendogly.presentation.ui.woof.model

import kotlinx.datetime.LocalDateTime

data class MyFootprint(
    val footprintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime,
)
