package com.happy.friendogly.presentation.ui.recentpet.adapter

import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.GetAllRecentPetUseCase
import com.happy.friendogly.presentation.ui.recentpet.RecentPetDateView
import com.happy.friendogly.presentation.ui.recentpet.RecentPetView
import com.happy.friendogly.presentation.ui.recentpet.RecentPetViewType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDateTime

class RecentPetListSource(private val getAllRecentPetUseCase: GetAllRecentPetUseCase) {
    suspend fun load(): List<RecentPetViewType> {
        return getAllRecentPetUseCase().fold(
            onSuccess = { recentPets ->
                var previousDate = java.time.LocalDate.now().toKotlinLocalDate()
                var index = 0
                val items = mutableListOf<RecentPetViewType>()
                recentPets.forEach { recentPet ->
                    if (isNewDate(previousDate, recentPet.createAt)) {
                        val date =
                            LocalDate(
                                recentPet.createAt.year,
                                recentPet.createAt.monthValue,
                                recentPet.createAt.dayOfMonth,
                            )
                        items.add(RecentPetDateView.from(index, date))
                        previousDate = date
                        index++
                    }

                    val last = recentPets.last() == recentPet
                    items.add(RecentPetView.from(index, recentPet, last))
                    index++
                }

                items
            },
            onError = {
                emptyList()
            },
        )
    }

    private fun isNewDate(
        previousDate: LocalDate,
        currentDate: LocalDateTime,
    ): Boolean {
        return previousDate.year != currentDate.year ||
            previousDate.monthNumber != currentDate.monthValue ||
            previousDate.dayOfMonth != currentDate.dayOfMonth
    }
}
