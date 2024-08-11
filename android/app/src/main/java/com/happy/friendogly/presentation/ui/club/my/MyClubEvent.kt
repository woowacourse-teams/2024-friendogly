package com.happy.friendogly.presentation.ui.club.my

sealed interface MyClubEvent {

    sealed interface Navigation : MyClubEvent {
        data class NavigateToClub(val clubId: Long) : Navigation

        data object NavigateToAddClub : Navigation
    }
}
