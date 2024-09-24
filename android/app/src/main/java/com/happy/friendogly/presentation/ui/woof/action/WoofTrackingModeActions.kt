package com.happy.friendogly.presentation.ui.woof.action

sealed interface WoofTrackingModeActions {
    data object NoFollowTrackingMode : WoofTrackingModeActions

    data object FollowTrackingMode : WoofTrackingModeActions

    data object FaceTrackingMode : WoofTrackingModeActions
}
