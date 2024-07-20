package com.woowacourse.friendogly.domain.model

import com.naver.maps.geometry.LatLng

data class FootPrint(
    val footPrintId: Long,
    val latLng: LatLng,
    val createdAt: String,
    val isMine: Boolean,
)
