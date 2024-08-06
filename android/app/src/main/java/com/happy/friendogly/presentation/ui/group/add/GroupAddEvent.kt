package com.happy.friendogly.presentation.ui.group.add

import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

sealed interface GroupAddEvent {
    sealed interface Navigation : GroupAddEvent {
        data object NavigateToHome : Navigation

        data object NavigateToSelectGroupPoster : Navigation

        data class NavigateToSelectDog(val filters: List<GroupFilter>) : Navigation
    }

    data class ChangePage(val page: Int) : GroupAddEvent

    data object FailLoadAddress : GroupAddEvent

    data object FailAddGroup : GroupAddEvent
}
