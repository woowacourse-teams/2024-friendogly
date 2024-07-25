package com.happy.friendogly.presentation.ui.group.list

sealed interface GroupListEvent {
    data class OpenGroup(val groupId: Long) : GroupListEvent

    sealed interface Navigation : GroupListEvent {
        data object NavigateToAddGroup : Navigation
    }
}
