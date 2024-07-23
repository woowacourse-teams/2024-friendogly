package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.WoofDataSource
import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import com.woowacourse.friendogly.domain.model.LandMark
import com.woowacourse.friendogly.domain.repository.WoofRepository
import com.woowacourse.friendogly.remote.model.request.FootprintRequest

class WoofRepositoryImpl(private val source: WoofDataSource) : WoofRepository {
    override suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit> {
        return source.postFootprint(
            FootprintRequest(
                latitude = latitude,
                longitude = longitude,
            ),
        )
    }

    override suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfo> {
        return source.getFootprintMarkBtnInfo().mapCatching { dto ->
            dto.toDomain()
        }
    }

    override suspend fun getNearFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>> {
        return source.getNearFootprints(latitude, longitude)
            .mapCatching { dto ->
                dto.toDomain()
            }
    }

    override suspend fun getLandMarks(): Result<List<LandMark>> {
        return source.getLandMarks().mapCatching { dto ->
            dto.toDomain()
        }
    }
}
