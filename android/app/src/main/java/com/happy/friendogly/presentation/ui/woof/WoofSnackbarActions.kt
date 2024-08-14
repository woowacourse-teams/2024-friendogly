package com.happy.friendogly.presentation.ui.woof

sealed interface WoofSnackbarActions {
    data object ShowHasNotPet : WoofSnackbarActions

    data class ShowCantClickMarkBtn(
        val remainingTime: Int,
    ) : WoofSnackbarActions

    data object ShowMarkerRegistered : WoofSnackbarActions

    data object ShowEndWalk : WoofSnackbarActions
}
