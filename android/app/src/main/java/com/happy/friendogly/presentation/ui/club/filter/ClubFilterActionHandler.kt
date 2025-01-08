package com.happy.friendogly.presentation.ui.club.filter

interface ClubFilterActionHandler {
    fun changeParticipationFilter(filterName: String)

    fun closeSheet()

    fun selectFilters()

    fun selectParticipationFilter()

    fun selectSizeGuide()
}
