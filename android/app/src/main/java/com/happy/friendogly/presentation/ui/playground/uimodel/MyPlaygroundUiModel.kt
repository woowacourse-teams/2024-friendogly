package com.happy.friendogly.presentation.ui.playground.uimodel

import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay

data class MyPlaygroundUiModel(
    val id: Long,
    val marker: Marker,
    val circleOverlay: CircleOverlay,
    val pathOverlay: PathOverlay,
)
