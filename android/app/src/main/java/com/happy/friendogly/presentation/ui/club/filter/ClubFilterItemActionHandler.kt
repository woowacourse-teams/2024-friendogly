package com.happy.friendogly.presentation.ui.club.filter

interface ClubFilterItemActionHandler {
    fun selectClubFilter(
        filterName: String,
        isSelected: Boolean,
    )
}
