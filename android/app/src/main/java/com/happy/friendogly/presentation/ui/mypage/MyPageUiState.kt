package com.happy.friendogly.presentation.ui.mypage

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate

data class MyPageUiState(
    val id: Long = -1,
    val nickname: String = "",
    val email: String = "",
    val tag: String = "",
    val profilePath: String? = null,
    val pets: List<PetViewType> = emptyList(),
)

interface PetViewType {
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
    override val id: Long = -1L,
    val memberId: Long,
) : PetViewType

// TODO 더미 데이터 모델
data class Dog(
    val name: String,
    val description: String,
    val birthDate: LocalDate,
    val sizeType: String,
    val gender: String,
    val isNeutered: Boolean,
    val image: String,
)
