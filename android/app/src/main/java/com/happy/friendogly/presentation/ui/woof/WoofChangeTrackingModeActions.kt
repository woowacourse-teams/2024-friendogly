package com.happy.friendogly.presentation.ui.woof

sealed interface WoofChangeTrackingModeActions {
    data object ChangeToNoFollowTrackingMode : WoofChangeTrackingModeActions

    data object ChangeToFollowTrackingMode : WoofChangeTrackingModeActions

    data object ChangeToFaceTrackingMode : WoofChangeTrackingModeActions
}
