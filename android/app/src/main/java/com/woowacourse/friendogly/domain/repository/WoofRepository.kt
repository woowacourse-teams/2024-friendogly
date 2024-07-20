package com.woowacourse.friendogly.domain.repository

import com.naver.maps.geometry.LatLng
import com.woowacourse.friendogly.domain.model.FootPrint
import com.woowacourse.friendogly.domain.model.LandMark

interface WoofRepository {
    suspend fun getNearFootPrints(latLng: LatLng): Result<List<FootPrint>>

    suspend fun getLandMarks(): Result<List<LandMark>>
}
