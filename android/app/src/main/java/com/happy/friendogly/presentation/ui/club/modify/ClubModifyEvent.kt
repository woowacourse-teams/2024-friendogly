package com.happy.friendogly.presentation.ui.club.modify

sealed interface ClubModifyEvent {
    sealed interface Navigation : ClubModifyEvent {
        data object NavigateToSelectClubPoster : Navigation

        data object NavigatePrev : Navigation

        data object NavigateSubmit : Navigation
    }
}
