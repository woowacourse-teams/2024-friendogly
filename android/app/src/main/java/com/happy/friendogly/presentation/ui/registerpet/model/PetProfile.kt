package com.happy.friendogly.presentation.ui.registerpet.model

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PetProfile(
    val id: Long,
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: SizeType,
    val gender: Gender,
    val imageUrl: String,
)
