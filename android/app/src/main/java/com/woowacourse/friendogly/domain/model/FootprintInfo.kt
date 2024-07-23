package com.woowacourse.friendogly.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class FootprintInfo(
    val memberName: String,
    val petName: String,
    val petDescription: String,
    val petBirthDate: LocalDate,
    val petSizeType: PetSizeType,
    val petGender: PetGender,
    val footprintImageUrl: String,
    val createdAt: LocalDateTime?,
    val isMine: Boolean,
)
