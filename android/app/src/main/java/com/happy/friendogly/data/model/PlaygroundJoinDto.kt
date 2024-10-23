package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDateTime

data class PlaygroundJoinDto(
    val playgroundId: Long,
    val memberId: Long,
    val message: String,
    val isArrived: Boolean,
    val exitTime: LocalDateTime?,
)
