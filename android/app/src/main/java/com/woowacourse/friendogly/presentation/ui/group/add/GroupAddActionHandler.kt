package com.woowacourse.friendogly.presentation.ui.group.add

interface GroupAddActionHandler {
    fun selectGroupFilter(
        filterName:String,
        isSelected: Boolean,
    )
}
