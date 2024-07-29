package com.happy.friendogly.presentation.ui.group.filter

interface GroupFilterActionHandler {
    fun changeParticipationFilter(filterName: String)

    fun closeSheet()

    fun selectFilters()

    fun selectParticipationFilter()
}
