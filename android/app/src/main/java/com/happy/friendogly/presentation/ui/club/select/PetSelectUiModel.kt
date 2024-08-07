package com.happy.friendogly.presentation.ui.club.select

import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.presentation.ui.club.mapper.toClubFilter
import com.happy.friendogly.presentation.ui.club.model.clubfilter.ClubFilter

data class PetSelectUiModel(
    val id: Long,
    val name: String,
    val profileImage: String,
    val size: ClubFilter.SizeFilter,
    val gender: ClubFilter.GenderFilter,
) {
    var isSelected = false
        private set

    fun selectDog() {
        isSelected = true
    }

    fun unSelectDog() {
        isSelected = false
    }
}

fun Pet.toPetSelectUiModel(): PetSelectUiModel {
    return PetSelectUiModel(
        id = id,
        name = name,
        profileImage = imageUrl,
        size = sizeType.toClubFilter(),
        gender = gender.toClubFilter(),
    )
}
