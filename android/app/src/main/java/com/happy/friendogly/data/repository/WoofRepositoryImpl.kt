package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo
import com.happy.friendogly.presentation.ui.woof.model.FootprintMarkBtnInfo
import com.happy.friendogly.presentation.ui.woof.model.FootprintSave
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.remote.model.request.FootprintRequest
import com.happy.friendogly.remote.model.request.WalkStatusRequest

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

    override suspend fun patchWalkStatus(
        latitude: Double,
        longitude: Double,
    ): Result<WalkStatus> {
        return source.patchWalkStatus(
            WalkStatusRequest(
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

    override suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfo> {
        return source.getFootprintInfo(footprintId)
            .mapCatching { dto -> dto.toDomain() }
    }
}
