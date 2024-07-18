package com.woowacourse.friendogly.presentation.ui.group.select

data class DogSelectUiModel(
    val id: Long,
    val name: String,
    val profileImage: String,
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
