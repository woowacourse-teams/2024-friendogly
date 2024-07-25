package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.FootprintInfo
import com.happy.friendogly.presentation.model.FootprintInfoUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Duration
import java.time.Period

fun FootprintInfo.toPresentation(): FootprintInfoUiModel {
    return FootprintInfoUiModel(
        petName = petName,
        petDescription = petDescription,
        petAge = petBirthDate.toAge(),
        petSizeType = petSizeType.petSizeType,
        petGender = petGender.petGender,
        footprintImageUrl = footprintImageUrl,
        dateOfVisit = createdAt.toDateOfVisit(),
        isMine = isMine,
    )
}

fun LocalDate.toAge(): String {
    val period = Period.between(this.toJavaLocalDate(), java.time.LocalDate.now())
    val years = period.years
    val months = period.months

    return if (years < 1) {
        "${months}개월"
    } else {
        "${years}살"
    }
}

fun LocalDateTime.toDateOfVisit(): String {
    val duration =
        Duration.between(this.toJavaLocalDateTime(), java.time.LocalDateTime.now())

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
