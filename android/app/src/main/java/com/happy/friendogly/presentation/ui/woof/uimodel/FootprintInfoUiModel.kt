package com.happy.friendogly.presentation.ui.woof.uimodel

import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import kotlinx.datetime.LocalDateTime

data class FootprintInfoUiModel(
    val memberName: String,
    val walkStatus: WalkStatus,
    val changedWalkStatusTime: LocalDateTime,
    val pets: List<PetDetailUiModel>,
    val isMine: Boolean,
)
