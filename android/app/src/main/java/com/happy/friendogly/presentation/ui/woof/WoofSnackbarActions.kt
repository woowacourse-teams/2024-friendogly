package com.happy.friendogly.presentation.ui.woof

sealed interface WoofSnackbarActions {
    data object ShowSettingSnackbar : WoofSnackbarActions

    data object ShowHasNotPetSnackbar : WoofSnackbarActions

    data class ShowCantClickMarkBtnSnackbar(
        val remainingTime: Int,
    ) : WoofSnackbarActions
}
