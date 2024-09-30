package com.happy.friendogly.presentation.ui.club.menu

sealed interface ClubMenuEvent {
    data object CancelSelection : ClubMenuEvent

    data object Modify : ClubMenuEvent

    data object Delete : ClubMenuEvent

    data object Report : ClubMenuEvent

    data object Block : ClubMenuEvent

    sealed interface Navigation : ClubMenuEvent {
        data object NavigateToPrev : Navigation
    }
}
