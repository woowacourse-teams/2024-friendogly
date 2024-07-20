package com.woowacourse.friendogly.presentation.ui.group.add

interface GroupAddActionHandler {
    fun selectGroupFilter(
        filterName: String,
        isSelected: Boolean,
    )

    fun selectAllSizeFilter(isSelected: Boolean)

    fun selectAllGenderFilter(isSelected: Boolean)
}
