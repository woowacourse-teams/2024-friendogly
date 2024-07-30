package com.happy.friendogly.presentation.model

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.model.WalkStatus
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class FootprintInfoUiModel(
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
