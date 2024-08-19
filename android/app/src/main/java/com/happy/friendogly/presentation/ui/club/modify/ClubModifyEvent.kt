package com.happy.friendogly.presentation.ui.club.modify

sealed interface ClubModifyEvent {
    sealed interface Navigation : ClubModifyEvent {

        data object NavigatePrev : Navigation

        data object NavigateSubmit : Navigation

        data object NavigateSelectState: Navigation
    }

    data object FailModify: ClubModifyEvent
}
