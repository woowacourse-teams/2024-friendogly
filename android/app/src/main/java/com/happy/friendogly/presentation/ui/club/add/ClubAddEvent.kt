package com.happy.friendogly.presentation.ui.club.add

import com.happy.friendogly.presentation.ui.club.model.clubfilter.ClubFilter

sealed interface ClubAddEvent {
    sealed interface Navigation : ClubAddEvent {
        data object NavigateToHome : Navigation

        data object NavigateToSelectClubPoster : Navigation

        data class NavigateToSelectDog(val filters: List<ClubFilter>) : Navigation
    }

    data class ChangePage(val page: Int) : ClubAddEvent

    data object FailLoadAddress : ClubAddEvent

    data object FailAddClub : ClubAddEvent
}
