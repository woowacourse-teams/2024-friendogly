package com.happy.friendogly.presentation.ui.woof.uimodel

import com.happy.friendogly.presentation.ui.playground.uimodel.PetDetailInfoUiModel
import com.naver.maps.map.overlay.Marker

data class FootprintInfoUiModel(
    val marker: Marker,
    val walkStatusInfo: WalkStatusInfoUiModel,
    val petsDetailInfo: List<PetDetailInfoUiModel>,
)
