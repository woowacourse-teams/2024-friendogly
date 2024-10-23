package com.happy.friendogly.presentation.ui.playground.action

import com.happy.friendogly.presentation.ui.playground.model.Playground
import com.naver.maps.geometry.LatLng

sealed interface PlaygroundMapAction {
    data class MakePlaygrounds(
        val myPlayground: Playground?,
        val nearPlaygrounds: List<Playground>,
    ) : PlaygroundMapAction

    data object RegisterMyPlayground : PlaygroundMapAction

    data object ShowRegisteringPlaygroundScreen : PlaygroundMapAction

    data object HideRegisteringPlaygroundScreen : PlaygroundMapAction

    data class MoveCameraCenterPosition(val position: LatLng) : PlaygroundMapAction

    data object ChangeBottomSheetBehavior : PlaygroundMapAction

    data object ChangeTrackingMode : PlaygroundMapAction

    data object StartLocationService : PlaygroundMapAction
}
