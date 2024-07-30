package com.happy.friendogly.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class FootprintInfo(
    val memberName: String,
    val petName: String,
    val petDescription: String,
    val petBirthDate: LocalDate,
    val petSizeType: SizeType,
    val petGender: Gender,
    val footprintImageUrl: String,
    val walkStatus: WalkStatus,
    val startWalkTime: LocalDateTime?,
    val endWalkTime: LocalDateTime?,
    val createdAt: LocalDateTime,
    val isMine: Boolean,
)
