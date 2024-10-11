package com.happy.friendogly.presentation.ui.club.filter

import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ParticipationFilter

sealed interface ClubFilterEvent {
    data object CancelSelection : ClubFilterEvent

    data object OpenSizeGuide: ClubFilterEvent

    data class SelectParticipation(val participationFilter: ParticipationFilter) : ClubFilterEvent

    data class SelectClubFilters(val filters: List<ClubFilter>) : ClubFilterEvent
}
