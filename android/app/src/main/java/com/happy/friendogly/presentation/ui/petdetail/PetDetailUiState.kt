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
