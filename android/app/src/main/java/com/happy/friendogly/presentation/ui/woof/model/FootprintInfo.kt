package com.happy.friendogly.presentation.ui.woof.model

import kotlinx.datetime.LocalDateTime

data class FootprintInfo(
    val memberName: String,
    val walkStatus: WalkStatus,
    val changedWalkStatusTime: LocalDateTime,
    val pets: List<PetDetail>,
    val isMine: Boolean,
)
