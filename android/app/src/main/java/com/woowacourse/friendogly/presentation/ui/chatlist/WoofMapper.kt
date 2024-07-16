package com.woowacourse.friendogly.presentation.ui.chatlist

import com.woowacourse.friendogly.remote.dto.response.ResponsePetGetDto
import java.time.LocalDate
import java.time.Period

fun ResponsePetGetDto.toUiModel(): WoofDogUiModel =
    WoofDogUiModel(
        image,
        name,
        sizeType,
        gender,
        calculateAge(birthDate),
        description,
    )

fun calculateAge(birthLocalDate: LocalDate): Int {
    val currentDate = LocalDate.now()
    return Period.between(birthLocalDate, currentDate).years
}
