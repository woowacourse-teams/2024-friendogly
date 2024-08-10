package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDate

data class PetDetailDto(
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: SizeTypeDto,
    val gender: GenderDto,
    val imageUrl: String,
)
