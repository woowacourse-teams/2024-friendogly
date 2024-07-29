package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.Footprint
import com.happy.friendogly.domain.model.FootprintMarkBtnInfo
import com.happy.friendogly.domain.model.FootprintSave

interface WoofRepository {
    suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<FootprintSave>

    suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfo>

    suspend fun getNearFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>>
}
