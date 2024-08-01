package com.happy.friendogly.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Pet(
    val id: Long,
    val memberId: Long,
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: SizeType,
    val gender: Gender,
    val imageUrl: String,
) {
    val age: Int
        get() {
            return if (isAtLeastOneYearOld) {
                currentDate.year - birthDate.year
            } else {
                currentDate.monthNumber - birthDate.monthNumber
            }
        }

    val isAtLeastOneYearOld: Boolean
        get() = currentDate.year - birthDate.year > 0

    companion object {
        private val now: Instant = Clock.System.now()
        private val timeZone = TimeZone.currentSystemDefault()
        private val currentDate = now.toLocalDateTime(timeZone).date
    }
}
