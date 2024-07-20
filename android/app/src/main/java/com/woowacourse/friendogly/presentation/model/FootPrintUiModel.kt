package com.woowacourse.friendogly.presentation.model

import com.naver.maps.geometry.LatLng

data class FootPrintUiModel(
    val latLng: LatLng,
    val createdAt: String,
    val isMine: Boolean,
)
