package com.happy.friendogly.presentation.model

import kotlinx.datetime.LocalDateTime

data class FootprintUiModel(
    val footprintId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime,
    val isMine: Boolean,
    val imageUrl: String?,
)
