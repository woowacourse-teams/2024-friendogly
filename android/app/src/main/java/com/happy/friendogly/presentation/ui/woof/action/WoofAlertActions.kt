package com.happy.friendogly.presentation.ui.woof.action

sealed interface WoofAlertActions {
    data object AlertHasNotLocationPermissionDialog : WoofAlertActions

    data object AlertHasNotPetDialog : WoofAlertActions

    data object AlertAddressOutOfKoreaSnackbar : WoofAlertActions

    data object AlertNotExistMyPlaygroundSnackbar : WoofAlertActions

    data object AlertMarkerRegisteredSnackbar : WoofAlertActions

    data object AlertLeaveMyPlaygroundSnackbar : WoofAlertActions

    data object AlertEndWalkSnackbar : WoofAlertActions

    data object AlertFailToCheckPetExistence : WoofAlertActions

    data object AlertFailToLoadPlaygroundsSnackbar : WoofAlertActions

    data object AlertFailToRegisterPlaygroundSnackbar : WoofAlertActions

    data object AlertFailToUpdatePlaygroundArrival : WoofAlertActions

    data object AlertFailToEndWalkSnackbar : WoofAlertActions

    data object AlertFailToDeleteMyFootprintSnackbar : WoofAlertActions

    data object AlertFailToLoadPlaygroundInfoSnackbar : WoofAlertActions

    data object AlertFailToLoadPlaygroundSummarySnackbar : WoofAlertActions

    data class AlertHelpBalloon(val textResId: Int) : WoofAlertActions
}
