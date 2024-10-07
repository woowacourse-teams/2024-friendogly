package com.happy.friendogly.presentation.ui.woof.uimodel

import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker

data class PlaygroundMarkerUiModel(
    val id: Long,
    val marker: Marker,
    val circleOverlay: CircleOverlay,
)
