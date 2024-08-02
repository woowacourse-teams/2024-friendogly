package com.happy.friendogly.presentation.ui.woof

sealed interface WoofMapActions {
    data object ChangeMapToNoFollowTrackingMode : WoofMapActions

    data object ChangeMapToFollowTrackingMode : WoofMapActions

    data object ChangeMapToFaceTrackingMode : WoofMapActions

    data object RemoveNearFootprints : WoofMapActions

    data object ShowRegisterMarkerLayout : WoofMapActions

    data object HideRegisterMarkerLayout : WoofMapActions
}
