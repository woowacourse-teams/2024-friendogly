package com.happy.friendogly.presentation.ui.woof.uimodel

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.LocalDate

data class PlaygroundPetDetailUiModel(
    val memberId: Long,
    val petId: Long,
    val name: String,
    val birthDate: LocalDate,
    val sizeType: SizeType,
    val gender: Gender,
    val imageUrl: String,
    val message: String,
    val isArrival: Boolean,
    val isMine: Boolean,
    val viewType: Int,
)
