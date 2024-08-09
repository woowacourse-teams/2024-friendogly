package com.happy.friendogly.presentation.ui.club.list

import com.happy.friendogly.presentation.ui.club.model.clubfilter.ClubFilter

interface ClubListActionHandler {
    fun loadClub(clubId: Long)

    fun addClub()

    fun selectParticipationFilter()

    fun selectSizeFilter()

    fun selectGenderFilter()

    fun removeFilter(clubFilter: ClubFilter)

    fun addMyLocation()
}
