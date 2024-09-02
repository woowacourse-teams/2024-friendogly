package com.happy.friendogly.presentation.ui.woof

sealed interface WoofTrackingModeActions {
    data object ToNoFollowTrackingMode : WoofTrackingModeActions

    data object ToFollowTrackingMode : WoofTrackingModeActions

    data object ToFaceTrackingMode : WoofTrackingModeActions
}
