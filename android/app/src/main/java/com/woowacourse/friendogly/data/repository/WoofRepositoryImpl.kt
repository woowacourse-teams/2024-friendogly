package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import com.woowacourse.friendogly.domain.model.LandMark
import com.woowacourse.friendogly.domain.repository.WoofRepository
import com.woowacourse.friendogly.remote.model.request.FootprintRequest
import com.woowacourse.friendogly.remote.source.WoofDataSource

class WoofRepositoryImpl(private val dataSource: WoofDataSource) : WoofRepository {
    override suspend fun postFootPrint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit> {
        return dataSource.postFootPrint(
            FootprintRequest(
                latitude = latitude,
                longitude = longitude,
            ),
        )
    }

    override suspend fun getFootPrintMarkBtnInfo(): Result<FootprintMarkBtnInfo> {
        return dataSource.getFootPrintMarkBtnInfo().mapCatching { dto ->
            dto.toDomain()
        }
    }

    override suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>> {
        return dataSource.getNearFootPrints(latitude, longitude)
            .mapCatching { dto ->
                dto.toDomain()
            }
    }

    override suspend fun getLandMarks(): Result<List<LandMark>> {
        return dataSource.getLandMarks().mapCatching { dto ->
            dto.toDomain()
        }
    }
}
