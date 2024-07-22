package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootprintInfo
import com.woowacourse.friendogly.domain.model.PetGender
import com.woowacourse.friendogly.domain.model.PetSizeType
import com.woowacourse.friendogly.presentation.model.FootprintInfoUiModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period

fun FootprintInfo.toPresentation(): FootprintInfoUiModel {
    return FootprintInfoUiModel(
        petName = "땡이",
        petDescription = "안녕하세요! 땡이에요~",
        petAge = LocalDate.of(2020, 2, 22).toAge(),
        petSizeType = PetSizeType.to(PetSizeType.SMALL),
        petGender = PetGender.to(PetGender.FEMALE),
        footprintImageUrl = "https://github.com/user-attachments/assets/9329234e-e47d-4fc5-b4b5-9f2a827b60b1",
        dateOfVisit =
            LocalDateTime.of(LocalDate.of(24, 7, 23), LocalTime.of(8, 30, 0))
                .toDateOfVisit(),
        isMine = false,
    )
//    return FootprintInfoUiModel(
//        petName = petName,
//        petDescription = petDescription,
//        petAge = petBirthDate.toAge(),
//        petSizeType = PetSizeType.to(petSizeType),
//        petGender = PetGender.to(petGender),
//        footprintImageUrl = footprintImageUrl,
//        dateOfVisit = createdAt.toDateOfVisit(),
//        isMine = isMine,
//    )
}

fun LocalDate.toAge(currentDate: LocalDate = LocalDate.now()): String {
    val period = Period.between(this, currentDate)
    val years = period.years
    val months = period.months

    return if (years < 1) {
        "${months}개월"
    } else {
        "${years}살"
    }
}

fun LocalDateTime.toDateOfVisit(currentDateTime: LocalDateTime = LocalDateTime.now()): String {
    val duration = Duration.between(this, currentDateTime)

    val minutes = duration.toMinutes()
    val hours = duration.toHours()
    val days = duration.toDays()

    return when {
        days > 0 -> "${days}일 전"
        hours > 0 -> "${hours}일 전"
        minutes > 0 -> "${minutes}일 전"
        else -> "방금 전"
    }
}
