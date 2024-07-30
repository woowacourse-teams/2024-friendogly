package com.happy.friendogly.presentation.model

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
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
    val createdAt: LocalDateTime,
    val isMine: Boolean,
)
