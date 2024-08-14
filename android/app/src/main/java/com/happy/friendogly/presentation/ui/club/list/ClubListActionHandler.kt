package com.happy.friendogly.presentation.ui.club.list

import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter

interface ClubListActionHandler {
    fun selectParticipationFilter()

    fun selectSizeFilter()

    fun selectGenderFilter()

    fun removeFilter(clubFilter: ClubFilter)

    fun addMyLocation()
}
