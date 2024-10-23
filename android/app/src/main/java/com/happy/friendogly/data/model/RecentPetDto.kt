package com.happy.friendogly.data.model

import kotlinx.datetime.LocalDate
import java.time.LocalDateTime

data class RecentPetDto(
    val id: Long = 0,
    val memberId: Long,
    val name: String,
    val birthday: LocalDate,
    val imgUrl: String,
    val gender: GenderDto,
    val sizeType: SizeTypeDto,
    val createAt: LocalDateTime,
)
