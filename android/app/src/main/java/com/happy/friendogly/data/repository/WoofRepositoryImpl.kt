package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus
import com.happy.friendogly.presentation.ui.woof.model.MyPlayground
import com.happy.friendogly.presentation.ui.woof.model.PetExistence
import com.happy.friendogly.presentation.ui.woof.model.Playground
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusAutoRequest
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusManualRequest
import com.happy.friendogly.remote.model.request.PlaygroundRequest
import javax.inject.Inject

class WoofRepositoryImpl
    @Inject
    constructor(private val source: WoofDataSource) : WoofRepository {
        override suspend fun postPlayground(
            latitude: Double,
            longitude: Double,
        ): Result<MyPlayground> {
            return source.postPlayground(
                PlaygroundRequest(
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

        override suspend fun getFootprintMarkBtnInfo(): Result<PetExistence> {
            return source.getFootprintMarkBtnInfo().mapCatching { dto ->
                dto.toDomain()
            }
        }

        override suspend fun getPlaygrounds(): Result<List<Playground>> {
            return source.getNearFootprints()
                .mapCatching { dto ->
                    dto.toDomain()
                }
        }

        override suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfo> {
            return source.getPlaygroundInfo(id)
                .mapCatching { dto -> dto.toDomain() }
        }

        override suspend fun deleteFootprint(footprintId: Long): Result<Unit> {
            return source.deleteFootprint(footprintId)
        }
    }
