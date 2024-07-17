package com.woowacourse.friendogly.presentation.ui.group.select

import java.io.Serializable

class DogSelectUiModel(
    val id: Long,
    val name: String,
    val profileImage: String,
    isSelected: Boolean,
): Serializable {
    var isSelected = isSelected
        private set

    fun selectDog(){
        isSelected = true
    }

    fun unSelectDog(){
        isSelected = false
    }
}
