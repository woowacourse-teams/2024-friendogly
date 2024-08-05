package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.FootprintDto
import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.data.model.FootprintMarkBtnInfoDto
import com.happy.friendogly.data.model.FootprintSaveDto
import com.happy.friendogly.remote.model.request.FootprintRequest

interface WoofDataSource {
    suspend fun postFootprint(request: FootprintRequest): Result<FootprintSaveDto>

    suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfoDto>

    suspend fun getNearFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootprintDto>>

    suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto>
}
