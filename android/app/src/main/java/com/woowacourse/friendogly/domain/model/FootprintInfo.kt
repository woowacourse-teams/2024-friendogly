package com.woowacourse.friendogly.domain.model

import java.time.LocalDate

data class FootprintInfo(
    val memberName: String,
    val petName: String,
    val petDescription: String,
    val petBirthDate: LocalDate,
    val petSizeType: PetSizeType,
    val petGender: PetGender,
    val footprintImageUrl: String,
    val isMine: Boolean,
)
