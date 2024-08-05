package com.happy.friendogly.presentation.ui.mypage

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class MyPageUiState(
    val id: Long = INVALID_ID,
    val nickname: String = "",
    val email: String = "",
    val tag: String = "",
    val imageUrl: String? = null,
    val pets: List<PetViewType> = emptyList(),
) {
    companion object {
        private const val INVALID_ID = -1L
    }
}

sealed interface PetViewType {
    val id: Long
}

data class PetView(
    override val id: Long,
    val name: String,
    val description: String,
    val birthDate: kotlinx.datetime.LocalDate,
    val sizeType: SizeType,
    val gender: Gender,
    val imageUrl: String,
) : PetViewType {
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

    private fun getCurrentDate(): kotlinx.datetime.LocalDate {
        val now: Instant = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        return now.toLocalDateTime(timeZone).date
    }

    companion object {
        const val MINIMUM_AGE = 0

        fun from(pet: Pet): PetView {
            return PetView(
                id = pet.id,
                name = pet.name,
                description = pet.description,
                birthDate = pet.birthDate,
                sizeType = pet.sizeType,
                gender = pet.gender,
                imageUrl = pet.imageUrl,
            )
        }
    }
}

data class PetAddView(
    override val id: Long = INVALID_ID,
    val memberId: Long,
) : PetViewType {
    companion object {
        private const val INVALID_ID = -1L
    }
}
