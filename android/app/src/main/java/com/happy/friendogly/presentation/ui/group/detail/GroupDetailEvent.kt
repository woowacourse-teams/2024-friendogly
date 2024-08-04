package com.happy.friendogly.presentation.ui.group.detail

import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailViewType
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

sealed interface GroupDetailEvent {
    data class OpenDogSelector(val filters: List<GroupFilter>) : GroupDetailEvent

    data class OpenDetailMenu(val groupDetailViewType: GroupDetailViewType) : GroupDetailEvent

    sealed interface Navigation : GroupDetailEvent {
        data object NavigateToChat : Navigation

        data object NavigateToHome : Navigation

        data class NavigateToProfile(val id: Long) : Navigation
    }
}
