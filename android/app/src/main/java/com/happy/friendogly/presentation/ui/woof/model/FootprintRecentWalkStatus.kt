package com.happy.friendogly.presentation.ui.woof.model

import kotlinx.datetime.LocalDateTime

data class FootprintRecentWalkStatus(
    val walkStatus: WalkStatus,
    val changedWalkStatusTime: LocalDateTime,
)
