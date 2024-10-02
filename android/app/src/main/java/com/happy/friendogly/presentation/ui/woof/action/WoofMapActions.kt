package com.happy.friendogly.presentation.ui.woof.action

import com.happy.friendogly.presentation.ui.woof.model.Playground
import com.naver.maps.geometry.LatLng

sealed interface WoofMapActions {
    data class MakeMyPlaygroundMarker(val myPlayground: Playground?) : WoofMapActions

    data class MakeNearPlaygroundMarkers(val nearPlaygrounds: List<Playground>) : WoofMapActions

    data class MoveCameraCenterPosition(val position: LatLng) : WoofMapActions

    data object RegisterMyFootprint : WoofMapActions

    data object ScanNearPlaygrounds : WoofMapActions

//    data object StopWalkTimeChronometer : WoofMapActions
}
