package com.happy.friendogly.presentation.ui.woof.uimodel

import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import kotlinx.datetime.LocalDateTime

data class WalkStatusInfoUiModel(
    val walkStatus: WalkStatus,
    val changedWalkStatusTime: LocalDateTime,
)
