package com.happy.friendogly.presentation.ui.woof

import com.happy.friendogly.presentation.ui.woof.model.Footprint

sealed interface WoofUiActions {
    data class MyFootprintLoaded(val myFootprint: Footprint?) : WoofUiActions

    data class NearFootprintsLoaded(val nearFootprints: List<Footprint>) : WoofUiActions

    data object ChangeToNoFollowTrackingMode : WoofUiActions

    data object ChangeToFollowTrackingMode : WoofUiActions

    data object ChangeToFaceTrackingMode : WoofUiActions
}
