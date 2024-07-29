package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.domain.model.Footprint
import com.happy.friendogly.domain.model.FootprintMarkBtnInfo
import com.happy.friendogly.domain.model.FootprintSave
import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.remote.model.request.FootprintRequest

class WoofRepositoryImpl(private val source: WoofDataSource) : WoofRepository {
    override suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<FootprintSave> {
        return source.postFootprint(
            FootprintRequest(
                latitude = latitude,
                longitude = longitude,
            ),
        ).mapCatching { dto ->
            dto.toDomain()
        }
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
}
