package com.happy.friendogly.domain.model

import kotlinx.datetime.LocalDate

data class Pet(
    val id: Long,
    val memberId: Long,
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: SizeType,
    val gender: Gender,
    val imageUrl: String,
)
