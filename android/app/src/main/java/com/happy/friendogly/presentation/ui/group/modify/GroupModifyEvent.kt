package com.happy.friendogly.presentation.ui.group.modify

sealed interface GroupModifyEvent {
    sealed interface Navigation : GroupModifyEvent {
        data object NavigateToSelectGroupPoster : Navigation

        data object NavigatePrev : Navigation

        data object NavigateSubmit : Navigation
    }
}
