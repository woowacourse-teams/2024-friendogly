package com.woowacourse.friendogly.presentation.ui.group.select

import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

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
