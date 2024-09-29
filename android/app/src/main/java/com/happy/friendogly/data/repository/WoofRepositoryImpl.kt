package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.FootprintMarkBtnInfo
import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus
import com.happy.friendogly.presentation.ui.woof.model.MyFootprint
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusAutoRequest
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusManualRequest
import com.happy.friendogly.remote.model.request.FootprintRequest
import javax.inject.Inject

class WoofRepositoryImpl
    @Inject
    constructor(private val source: WoofDataSource) : WoofRepository {
        override suspend fun postFootprint(
            latitude: Double,
            longitude: Double,
        ): Result<MyFootprint> {
            return source.postFootprint(
                FootprintRequest(
                    latitude = latitude,
                    longitude = longitude,
                ),
            ).mapCatching { dto ->
                dto.toDomain()
            }
        }

        override suspend fun patchFootprintRecentWalkStatusAuto(
            latitude: Double,
            longitude: Double,
        ): Result<FootprintRecentWalkStatus> {
            return source.patchFootprintRecentWalkStatusAuto(
                FootprintRecentWalkStatusAutoRequest(
                    latitude = latitude,
                    longitude = longitude,
                ),
            ).mapCatching { dto ->
                dto.toDomain()
            }
        }

        override suspend fun patchFootprintRecentWalkStatusManual(walkStatus: WalkStatus): Result<FootprintRecentWalkStatus> {
            return source.patchFootprintRecentWalkStatusManual(
                FootprintRecentWalkStatusManualRequest(walkStatus = walkStatus),
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

        override suspend fun getFootprintInfo(footprintId: Long): Result<PlaygroundInfo> {
            return source.getFootprintInfo(footprintId)
                .mapCatching { dto -> dto.toDomain() }
        }

        override suspend fun deleteFootprint(footprintId: Long): Result<Unit> {
            return source.deleteFootprint(footprintId)
        }
    }
