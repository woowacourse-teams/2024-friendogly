package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.domain.model.FootPrint
import com.woowacourse.friendogly.domain.model.FootPrintMarkBtnInfo
import com.woowacourse.friendogly.domain.model.LandMark
import com.woowacourse.friendogly.domain.repository.WoofRepository
import com.woowacourse.friendogly.remote.model.request.FootPrintRequest
import com.woowacourse.friendogly.remote.source.WoofDataSource

class WoofRepositoryImpl(private val dataSource: WoofDataSource) : WoofRepository {
    override suspend fun postFootPrint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit> {
        return dataSource.postFootPrint(
            FootPrintRequest(
                latitude = latitude,
                longitude = longitude,
            ),
        )
    }

    override suspend fun getFootPrintMarkBtnInfo(): Result<FootPrintMarkBtnInfo> {
        return dataSource.getFootPrintMarkBtnInfo().mapCatching { dto ->
            dto.toDomain()
        }
    }

    override suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootPrint>> {
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
