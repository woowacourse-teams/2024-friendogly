package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootprintInfo
import com.woowacourse.friendogly.domain.model.PetGender
import com.woowacourse.friendogly.domain.model.PetSizeType
import com.woowacourse.friendogly.presentation.model.FootprintInfoUiModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

fun FootprintInfo.toPresentation(): FootprintInfoUiModel {
    return FootprintInfoUiModel(
        petName = petName,
        petDescription = petDescription,
        petAge = petBirthDate.toAge(),
        petSizeType = PetSizeType.to(petSizeType),
        petGender = PetGender.to(petGender),
        footprintImageUrl = footprintImageUrl,
        dateOfVisit = createdAt.toDateOfVisit(),
        isMine = isMine,
    )
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

fun LocalDateTime?.toDateOfVisit(currentDateTime: LocalDateTime = LocalDateTime.now()): String {
    if (this == null) {
        return "방금 전"
    }
    val duration = Duration.between(this, currentDateTime)

    val minutes = duration.toMinutes()
    val hours = duration.toHours()
    val days = duration.toDays()

    return when {
        days > 0 -> "${days}일 전"
        hours > 0 -> "${hours}시간 전"
        minutes > 0 -> "${minutes}분 전"
        else -> "방금 전"
    }
}
