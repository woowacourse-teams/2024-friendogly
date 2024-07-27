package com.happy.friendogly.presentation.ui.group.filter

interface GroupFilterItemActionHandler {
    fun selectGroupFilter(
        filterName: String,
        isSelected: Boolean,
    )
}
