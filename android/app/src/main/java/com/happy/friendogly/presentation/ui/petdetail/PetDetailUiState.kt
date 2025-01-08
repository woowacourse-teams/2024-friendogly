package com.happy.friendogly.presentation.ui.petdetail

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

data class PetDetailUiState(
    val petsDetail: PetsDetail = PetsDetail(emptyList()),
    val startPage: Int = 0,
)

@Serializable
data class PetsDetail(
    val data: List<PetDetail>,
)

@Serializable
data class PetDetail(
    val id: Long,
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: SizeType,
    val gender: Gender,
    val imageUrl: String,
) {
    val age: Int
        get() {
            val currentDate = getCurrentDate()
            return if (isAtLeastOneYearOld) {
                currentDate.year - birthDate.year
            } else {
                currentDate.monthNumber - birthDate.monthNumber
            }
        }

    val isAtLeastOneYearOld: Boolean
        get() = getCurrentDate().year - birthDate.year > MINIMUM_AGE

    private fun getCurrentDate(): LocalDate {
        val now: Instant = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        return now.toLocalDateTime(timeZone).date
    }

    companion object {
        const val MINIMUM_AGE = 0
    }
}
