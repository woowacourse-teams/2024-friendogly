package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.data.source.FootprintDataSource
import com.woowacourse.friendogly.remote.api.FootprintService
import com.woowacourse.friendogly.remote.mapper.toData
import com.woowacourse.friendogly.remote.model.request.PostFootprintsRequest

class FootprintDataSourceImpl(
    private val service: FootprintService,
) : FootprintDataSource {
    override suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit> =
        runCatching {
            val body = PostFootprintsRequest(latitude = latitude, longitude = longitude)
            service.postFootprint(body = body).data
        }

    override suspend fun getFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootprintDto>> =
        runCatching {
            service.getFootprints(latitude = latitude, longitude = longitude).data.toData()
        }
}
