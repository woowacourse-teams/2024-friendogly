package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.PlaygroundDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
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
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class PlaygroundRepositoryImpl
    @Inject
    constructor(
        private val source: PlaygroundDataSource,
    ) : PlaygroundRepository {
        override suspend fun postPlayground(
            latitude: Double,
            longitude: Double,
        ): DomainResult<MyPlayground, DataError.Network> {
            return source.postPlayground(
                PostPlaygroundRequest(
                    latitude = latitude,
                    longitude = longitude,
                ),
            ).fold(
                onSuccess = { dto ->
                    DomainResult.Success(dto.toDomain())
                },
                onFailure = { e ->
                    when (e) {
                        is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                        is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                    }
                },
            )

//        return source
//            .postPlayground(
//                PostPlaygroundRequest(
//                    latitude = latitude,
//                    longitude = longitude,
//                ),
//            ).mapCatching { dto ->
//                dto.toDomain()
//
//            }
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
