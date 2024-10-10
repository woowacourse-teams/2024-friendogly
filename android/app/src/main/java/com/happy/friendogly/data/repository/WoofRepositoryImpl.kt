package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.MyPlayground
import com.happy.friendogly.presentation.ui.woof.model.PetExistence
import com.happy.friendogly.presentation.ui.woof.model.Playground
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundArrival
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundJoin
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundSummary
import com.happy.friendogly.remote.model.request.PatchPlaygroundArrivalRequest
import com.happy.friendogly.remote.model.request.PostPlaygroundRequest
import javax.inject.Inject

class WoofRepositoryImpl
    @Inject
    constructor(private val source: WoofDataSource) : WoofRepository {
        override suspend fun postPlayground(
            latitude: Double,
            longitude: Double,
        ): Result<MyPlayground> {
            return source.postPlayground(
                PostPlaygroundRequest(
                    latitude = latitude,
                    longitude = longitude,
                ),
            ).mapCatching { dto ->
                dto.toDomain()
            }
        }

        override suspend fun patchPlaygroundArrival(
            latitude: Double,
            longitude: Double,
        ): Result<PlaygroundArrival> {
            return source.patchPlaygroundArrival(
                PatchPlaygroundArrivalRequest(
                    latitude = latitude,
                    longitude = longitude,
                ),
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

        override suspend fun getPlaygroundSummary(playgroundId: Long): Result<PlaygroundSummary> {
            return source.getPlaygroundSummary(playgroundId)
                .mapCatching { dto -> dto.toDomain() }
        }

        override suspend fun postPlaygroundJoin(playgroundId: Long): Result<PlaygroundJoin> {
            return source.postPlaygroundJoin(playgroundId)
                .mapCatching { dto -> dto.toDomain() }
        }

        override suspend fun deletePlaygroundLeave(): Result<Unit> {
            return source.deletePlaygroundLeave()
        }
    }
