package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDateTime

data class FootprintRecentWalkStatusDto(
    val walkStatus: WalkStatusDto,
    val changedWalkStatusTime: LocalDateTime,
)
