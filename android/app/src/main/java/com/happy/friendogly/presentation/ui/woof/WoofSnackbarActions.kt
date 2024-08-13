package com.happy.friendogly.presentation.ui.woof

sealed interface WoofSnackbarActions {
    data object ShowHasNotPetSnackbar : WoofSnackbarActions

    data class ShowCantClickMarkBtnSnackbar(
        val remainingTime: Int,
    ) : WoofSnackbarActions

    data object ShowMarkerRegistered : WoofSnackbarActions

    data object ShowInvalidLocationSnackbar : WoofSnackbarActions

    data object ShowBeforeWalkStatusSnackbar : WoofSnackbarActions

    data object ShowOnGoingWalkStatusSnackbar : WoofSnackbarActions

    data object ShowAfterWalkStatusSnackbar : WoofSnackbarActions
}
