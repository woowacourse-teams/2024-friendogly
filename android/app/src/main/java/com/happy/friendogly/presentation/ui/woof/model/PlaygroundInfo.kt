package com.happy.friendogly.presentation.ui.woof.model

import kotlinx.datetime.LocalDateTime

data class PlaygroundInfo(
    val memberId: Long,
    val memberName: String,
    val walkStatus: WalkStatus,
    val changedWalkStatusTime: LocalDateTime,
    val petDetails: List<PetDetail>,
    val isMine: Boolean,
)
