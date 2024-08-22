package com.happy.friendogly.presentation.ui.woof

import com.happy.friendogly.presentation.ui.woof.model.Footprint

sealed interface WoofMakeMarkerActions {
    data class MakeMyFootprintMarker(val myFootprint: Footprint?) : WoofMakeMarkerActions

    data class MakeNearFootprintMarkers(val nearFootprints: List<Footprint>) : WoofMakeMarkerActions
}
