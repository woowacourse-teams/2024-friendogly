package com.happy.friendogly.presentation.ui.group.filter

interface GroupFilterActionHandler {
    fun selectGroupFilter(
        filterName: String,
        isSelected: Boolean,
    )
}
