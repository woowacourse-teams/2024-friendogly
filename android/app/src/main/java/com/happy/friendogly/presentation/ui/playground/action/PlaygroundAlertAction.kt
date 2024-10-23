package com.happy.friendogly.presentation.ui.playground.action

sealed interface PlaygroundAlertAction {
    data object AlertHasNotLocationPermissionDialog : PlaygroundAlertAction

    data object AlertHasNotPetDialog : PlaygroundAlertAction

    data object AlertLeaveAndRegisterPlaygroundDialog : PlaygroundAlertAction

    data object AlertLeaveAndJoinPlaygroundDialog : PlaygroundAlertAction

    data object AlertAddressOutOfKoreaSnackbar : PlaygroundAlertAction

    data object AlertNotExistMyPlaygroundSnackbar : PlaygroundAlertAction

    data object AlertPlaygroundRegisteredSnackbar : PlaygroundAlertAction

    data object AlertLeaveMyPlaygroundSnackbar : PlaygroundAlertAction

    data object AlertJoinPlaygroundSnackbar : PlaygroundAlertAction

    data object AlertAutoLeavePlaygroundSnackbar : PlaygroundAlertAction

    data object AlertOverlapPlaygroundCreationSnackbar : PlaygroundAlertAction

    data object AlertFailToCheckPetExistence : PlaygroundAlertAction

    data object AlertFailToLoadPlaygroundsSnackbar : PlaygroundAlertAction

    data object AlertFailToRegisterPlaygroundSnackbar : PlaygroundAlertAction

    data object AlertFailToUpdatePlaygroundArrival : PlaygroundAlertAction

    data object AlertFailToLeavePlaygroundSnackbar : PlaygroundAlertAction

    data object AlertFailToSwitchPlaygroundSnackbar : PlaygroundAlertAction

    data object AlertFailToLoadPlaygroundInfoSnackbar : PlaygroundAlertAction

    data object AlertFailToLoadPlaygroundSummarySnackbar : PlaygroundAlertAction

    data object AlertFailToJoinPlaygroundSnackbar : PlaygroundAlertAction

    data class AlertHelpBalloon(val textResId: Int) : PlaygroundAlertAction
}
