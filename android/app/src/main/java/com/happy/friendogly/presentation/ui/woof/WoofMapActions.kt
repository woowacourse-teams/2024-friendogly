package com.happy.friendogly.presentation.ui.woof

sealed interface WoofMapActions {
    data object ChangeMapToNoFollowTrackingMode : WoofMapActions

    data object ChangeMapToFollowTrackingMode : WoofMapActions

    data object ChangeMapToFaceTrackingMode : WoofMapActions
}
