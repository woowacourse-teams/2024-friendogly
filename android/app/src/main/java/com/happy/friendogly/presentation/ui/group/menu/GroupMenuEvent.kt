package com.happy.friendogly.presentation.ui.group.menu

sealed interface GroupMenuEvent {
    data object CancelSelection : GroupMenuEvent

    data object Modify : GroupMenuEvent

    data object Delete : GroupMenuEvent

    data object Report : GroupMenuEvent

    data object Block : GroupMenuEvent

    sealed interface Navigation: GroupMenuEvent {
        data object NavigateToPrev : Navigation
    }
}
