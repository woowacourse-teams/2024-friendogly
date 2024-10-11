package com.happy.friendogly.presentation.ui.playground.action

import com.happy.friendogly.presentation.ui.playground.model.Playground
import com.naver.maps.geometry.LatLng

sealed interface PlaygroundMapAction {
    data class MakeMyPlaygroundMarker(val myPlayground: Playground) : PlaygroundMapAction

    data class MakeNearPlaygroundMarkers(val nearPlaygrounds: List<Playground>) : PlaygroundMapAction

    data class MoveCameraCenterPosition(val position: LatLng) : PlaygroundMapAction

    data object RegisterMyPlayground : PlaygroundMapAction

    data object ScanNearPlaygrounds : PlaygroundMapAction

//    data object StopWalkTimeChronometer : WoofMapActions
}
