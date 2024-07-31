package com.happy.friendogly.presentation.ui.woof.uimodel

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.LocalDate

data class PetDetailUiModel(
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: SizeType,
    val gender: Gender,
    val imageUrl: String,
)
