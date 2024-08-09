package com.happy.friendogly.presentation.ui.club.list

import com.happy.friendogly.presentation.ui.club.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.model.clubfilter.ParticipationFilter

sealed interface ClubListEvent {
    data class OpenClub(val clubId: Long) : ClubListEvent

    data class OpenParticipationFilter(val participationFilter: ParticipationFilter) : ClubListEvent

    data class OpenFilterSelector(
        val clubFilterType: ClubFilter,
        val clubFilters: List<ClubFilter>,
    ) : ClubListEvent

    sealed interface Navigation : ClubListEvent {
        data object NavigateToAddClub : Navigation

        data object NavigateToAddress : Navigation
    }
}
