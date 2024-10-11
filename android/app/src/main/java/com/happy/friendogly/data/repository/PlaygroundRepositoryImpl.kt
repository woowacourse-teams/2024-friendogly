package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.PlaygroundDataSource
import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.MyPlayground
import com.happy.friendogly.presentation.ui.playground.model.PetExistence
import com.happy.friendogly.presentation.ui.playground.model.Playground
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundArrival
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundJoin
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundSummary
import com.happy.friendogly.remote.model.request.PatchPlaygroundArrivalRequest
import com.happy.friendogly.remote.model.request.PostPlaygroundRequest
import javax.inject.Inject

class PlaygroundRepositoryImpl
    @Inject
    constructor(
        private val source: PlaygroundDataSource,
    ) : PlaygroundRepository {
        override suspend fun postPlayground(
            latitude: Double,
            longitude: Double,
        ): Result<MyPlayground> =
            source
                .postPlayground(
                    PostPlaygroundRequest(
                        latitude = latitude,
                        longitude = longitude,
                    ),
                ).mapCatching { dto ->
                    dto.toDomain()
                }

        override suspend fun patchPlaygroundArrival(
            latitude: Double,
            longitude: Double,
        ): Result<PlaygroundArrival> =
            source
                .patchPlaygroundArrival(
                    PatchPlaygroundArrivalRequest(
                        latitude = latitude,
                        longitude = longitude,
                    ),
                ).mapCatching { dto ->
                    dto.toDomain()
                }

        override suspend fun getFootprintMarkBtnInfo(): Result<PetExistence> =
            source.getFootprintMarkBtnInfo().mapCatching { dto ->
                dto.toDomain()
            }

        override suspend fun getPlaygrounds(): Result<List<Playground>> =
            source
                .getNearFootprints()
                .mapCatching { dto ->
                    dto.toDomain()
                }

        override suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfo> =
            source
                .getPlaygroundInfo(id)
                .mapCatching { dto -> dto.toDomain() }

        override suspend fun getPlaygroundSummary(playgroundId: Long): Result<PlaygroundSummary> =
            source
                .getPlaygroundSummary(playgroundId)
                .mapCatching { dto -> dto.toDomain() }

        override suspend fun postPlaygroundJoin(playgroundId: Long): Result<PlaygroundJoin> =
            source
                .postPlaygroundJoin(playgroundId)
                .mapCatching { dto -> dto.toDomain() }

        override suspend fun deletePlaygroundLeave(): Result<Unit> = source.deletePlaygroundLeave()
    }
