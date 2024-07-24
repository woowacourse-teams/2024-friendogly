package com.woowacourse.friendogly.presentation.model

data class FootprintUiModel(
    val footprintId: Long,
    val latitude: Double,
    val longitude: Double,
    val isMine: Boolean,
    val imageUrl: String?,
)
