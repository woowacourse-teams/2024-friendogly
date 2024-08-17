package com.happy.friendogly.presentation.ui.woof

import com.happy.friendogly.presentation.ui.woof.model.Footprint

sealed interface WoofMapActions {
    data class MyFootprintLoaded(val myFootprint: Footprint?) : WoofMapActions

    data class NearFootprintsLoaded(val nearFootprints: List<Footprint>) : WoofMapActions

    data object ChangeMapToNoFollowTrackingMode : WoofMapActions

    data object ChangeMapToFollowTrackingMode : WoofMapActions

    data object ChangeMapToFaceTrackingMode : WoofMapActions

    data object ShowRegisterMarkerLayout : WoofMapActions

    data object HideRegisterMarkerLayout : WoofMapActions
}
