package com.happy.friendogly.presentation.ui.woof

sealed interface WoofAlertActions {
    data object AlertHasNotPetDialog : WoofAlertActions

    data class AlertCantClickMarkBtnSnackbar(
        val remainingTime: Int,
    ) : WoofAlertActions

    data object AlertMarkerRegisteredSnackbar : WoofAlertActions

    data object AlertDeleteMyFootprintMarkerSnackbar : WoofAlertActions

    data object AlertEndWalkSnackbar : WoofAlertActions
}
