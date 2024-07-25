package com.happy.friendogly.presentation.ui.woof

sealed interface WoofMapActions {
    data object MarkFootPrint : WoofMapActions

    data object ChangeMapToNoFollowTrackingMode : WoofMapActions

    data object ChangeMapToFollowTrackingMode : WoofMapActions

    data object ChangeMapToFaceTrackingMode : WoofMapActions
}
