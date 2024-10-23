package com.happy.friendogly.presentation.ui.playground.uimodel

import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker

data class PlaygroundUiModel(
    val id: Long,
    val marker: Marker,
    val circleOverlay: CircleOverlay,
)
