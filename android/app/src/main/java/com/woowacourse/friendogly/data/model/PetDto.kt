package com.woowacourse.friendogly.data.model

import kotlinx.datetime.LocalDate

data class PetDto(
    val id: Long,
    val memberId: Long,
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: SizeTypeDto,
    val gender: GenderDto,
    val imageUrl: String,
)
