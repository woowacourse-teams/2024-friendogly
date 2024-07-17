package com.woowacourse.friendogly.presentation.ui.group.select

import java.io.Serializable

data class DogSelectUiModel(
    val id: Long,
    val name: String,
    val profileImage: String,
) : Serializable {
    var isSelected = false
        private set

    fun selectDog()  {
        isSelected = true
    }

    fun unSelectDog()  {
        isSelected = false
    }
}
