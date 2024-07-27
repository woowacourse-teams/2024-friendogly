package com.happy.friendogly.presentation.ui.group.filter

import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter

sealed interface GroupFilterEvent {
    data object CancelSelection: GroupFilterEvent

    data class SelectParticipation(val participationFilter: ParticipationFilter): GroupFilterEvent

    data class SelectGroupFilters(val filters: List<GroupFilter>): GroupFilterEvent
}
