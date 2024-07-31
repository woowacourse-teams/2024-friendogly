package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDateTime

data class FootprintInfoDto(
    val memberName: String,
    val walkStatus: WalkStatusDto,
    val changedWalkStatusTime: LocalDateTime,
    val pets: List<PetDetailDto>,
    val isMine: Boolean,
)
