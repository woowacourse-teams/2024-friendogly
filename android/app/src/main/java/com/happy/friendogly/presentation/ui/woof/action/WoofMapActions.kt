package com.happy.friendogly.presentation.ui.woof.action

import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.naver.maps.geometry.LatLng

sealed interface WoofMapActions {
    data class MakeMyFootprintMarker(val myFootprint: Footprint?) : WoofMapActions

    data class MakeNearFootprintMarkers(val nearFootprints: List<Footprint>) : WoofMapActions

    data class MoveCameraCenterPosition(val position: LatLng) : WoofMapActions

    data object RegisterMyFootprint : WoofMapActions

    data object ScanNearFootprints : WoofMapActions

    data object StopWalkTimeChronometer : WoofMapActions
}
