package com.happy.friendogly.presentation.ui.recentpet

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.RecentPet
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class RecentPetUiState(val recentPets: List<RecentPetViewType> = emptyList())

enum class RoundState {
    FIRST,
    MID,
    LAST,
    FIRST_AND_LAST,
}

sealed interface RecentPetViewType {
    val index: Int
    val last: Boolean

    val roundState: RoundState
        get() =
            if (index == 0) {
                if (last) {
                    RoundState.FIRST_AND_LAST
                } else {
                    RoundState.FIRST
                }
            } else {
                if (last) {
                    RoundState.LAST
                } else {
                    RoundState.MID
                }
            }
}

data class RecentPetView(
    override val index: Int,
    override val last: Boolean,
    val memberId: Long,
    val name: String,
    val imageUrl: String,
    val birthday: LocalDate,
    val gender: Gender,
    val sizeType: SizeType,
) : RecentPetViewType {
    val age: Int
        get() {
            val currentDate = getCurrentDate()
            return if (isAtLeastOneYearOld) {
                currentDate.year - birthday.year
            } else {
                currentDate.monthNumber - birthday.monthNumber
            }
        }

    val isAtLeastOneYearOld: Boolean
        get() = getCurrentDate().year - birthday.year > MINIMUM_AGE

    private fun getCurrentDate(): LocalDate {
        val now: Instant = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        return now.toLocalDateTime(timeZone).date
    }

    companion object {
        const val MINIMUM_AGE = 0

        fun from(
            index: Int,
            recentPet: RecentPet,
            last: Boolean,
        ): RecentPetView {
            return RecentPetView(
                index = index,
                memberId = recentPet.memberId,
                imageUrl = recentPet.imgUrl,
                name = recentPet.name,
                birthday = recentPet.birthday,
                gender = recentPet.gender,
                sizeType = recentPet.sizeType,
                last = last,
            )
        }
    }
}

data class RecentPetDateView(
    override val index: Int,
    override val last: Boolean = false,
    val date: LocalDate,
) : RecentPetViewType {
    companion object {
        fun from(
            index: Int,
            date: LocalDate,
        ): RecentPetDateView {
            return RecentPetDateView(
                index = index,
                date = date,
            )
        }
    }
}
