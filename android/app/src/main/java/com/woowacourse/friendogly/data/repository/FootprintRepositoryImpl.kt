package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.FootprintDataSource
import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.domain.repository.FootprintRepository

class FootprintRepositoryImpl(
    private val source: FootprintDataSource,
) : FootprintRepository {
    override suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit> = source.postFootprint(latitude = latitude, longitude = longitude)

    override suspend fun getFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>> =
        source.getFootprints(latitude = latitude, longitude = longitude).mapCatching { result ->
            result.toDomain()
        }
}
