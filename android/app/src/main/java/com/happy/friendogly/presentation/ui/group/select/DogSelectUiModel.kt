package com.happy.friendogly.presentation.ui.group.select

import com.happy.friendogly.presentation.ui.group.mapper.toGroupFilter
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

data class DogSelectUiModel(
    val id: Long,
    val name: String,
    val profileImage: String,
    val size: GroupFilter.SizeFilter,
    val gender: GroupFilter.GenderFilter,
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

fun Pet.toDogSelectUiModel(): DogSelectUiModel {
    return DogSelectUiModel(
        id = id,
        name = name,
        profileImage = imageUrl,
        size = sizeType.toGroupFilter(),
        gender = gender.toGroupFilter(),
    )
}
