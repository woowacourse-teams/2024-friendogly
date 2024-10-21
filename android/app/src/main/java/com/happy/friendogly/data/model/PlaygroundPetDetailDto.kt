package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDate

data class PlaygroundPetDetailDto(
    val memberId: Long,
    val petId: Long,
    val name: String,
    val birthDate: LocalDate,
    val sizeType: SizeTypeDto,
    val gender: GenderDto,
    val imageUrl: String,
    val message: String,
    val isArrival: Boolean,
    val isMine: Boolean,
)
