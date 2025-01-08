package com.happy.friendogly.domain.model

import kotlinx.datetime.LocalDate
import java.time.LocalDateTime

data class RecentPet(
    val id: Long = 0,
    val memberId: Long,
    val name: String,
    val birthday: LocalDate,
    val imgUrl: String,
    val gender: Gender,
    val sizeType: SizeType,
    val createAt: LocalDateTime,
)
