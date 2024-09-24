package com.happy.friendogly.presentation.ui.woof.action

sealed interface WoofAlertActions {
    data object AlertHasNotLocationPermissionDialog : WoofAlertActions

    data object AlertHasNotPetDialog : WoofAlertActions

    data class AlertMarkBtnClickBeforeTimeoutSnackbar(
        val remainingTime: Int,
    ) : WoofAlertActions

    data object AlertAddressOutOfKoreaSnackbar : WoofAlertActions

    data object AlertNotExistMyFootprintSnackbar : WoofAlertActions

    data object AlertMarkerRegisteredSnackbar : WoofAlertActions

    data object AlertDeleteMyFootprintMarkerSnackbar : WoofAlertActions

    data object AlertEndWalkSnackbar : WoofAlertActions

    data object AlertFailToLoadFootprintMarkBtnInfoSnackbar : WoofAlertActions

    data object AlertFailToLoadNearFootprintsSnackbar : WoofAlertActions

    data object AlertFailToRegisterFootprintSnackbar : WoofAlertActions

    data object AlertFailToLoadFootprintInfoSnackbar : WoofAlertActions

    data object AlertFailToUpdateFootprintWalkStatusSnackbar : WoofAlertActions

    data object AlertFailToEndWalkSnackbar : WoofAlertActions

    data object AlertFailToDeleteMyFootprintSnackbar : WoofAlertActions

    data class AlertHelpBalloon(val textResId: Int) : WoofAlertActions
}
