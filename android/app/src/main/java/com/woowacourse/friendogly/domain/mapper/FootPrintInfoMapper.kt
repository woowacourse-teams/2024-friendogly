package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootPrintInfo
import com.woowacourse.friendogly.domain.model.PetGender
import com.woowacourse.friendogly.domain.model.PetSizeType
import com.woowacourse.friendogly.presentation.model.FootPrintInfoUiModel
import java.time.LocalDate
import java.time.Period

fun FootPrintInfo.toPresentation(): FootPrintInfoUiModel {
    return FootPrintInfoUiModel(
        petName = "땡이",
        petDescription = "안녕하세요! 땡이에요~",
        petAge = LocalDate.of(2020, 2, 22).toAge(),
        petSizeType = PetSizeType.to(PetSizeType.SMALL),
        petGender = PetGender.to(PetGender.FEMALE),
        footprintImageUrl = "https://github.com/user-attachments/assets/9329234e-e47d-4fc5-b4b5-9f2a827b60b1",
        isMine = false,
    )
//    return FootPrintInfoUiModel(
//        petName = petName,
//        petDescription = petDescription,
//        petAge = petBirthDate.toAge(),
//        petSizeType = PetSizeType.to(petSizeType),
//        petGender = PetGender.to(petGender),
//        footprintImageUrl = footprintImageUrl,
//        isMine = isMine,
//    )
}

fun LocalDate.toAge(currentLocalDate: LocalDate = LocalDate.now()): String {
    val period = Period.between(this, currentLocalDate)
    val years = period.years
    val months = period.months

    return if (years < 1) {
        "${months}개월"
    } else {
        "${years}살"
    }
}
