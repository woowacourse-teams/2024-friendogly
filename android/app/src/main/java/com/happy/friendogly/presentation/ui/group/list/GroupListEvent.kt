package com.happy.friendogly.presentation.ui.group.list

import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter

sealed interface GroupListEvent {
    data class OpenGroup(val groupId: Long) : GroupListEvent

    data class OpenParticipationFilter(val participationFilter: ParticipationFilter) : GroupListEvent

    data class OpenFilterSelector(
        val groupFilterType: GroupFilter,
        val groupFilters: List<GroupFilter>,
    ) : GroupListEvent

    sealed interface Navigation : GroupListEvent {
        data object NavigateToAddGroup : Navigation
    }
}
