package com.happy.friendogly.data.repository

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
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundMessage
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundSummary
import com.happy.friendogly.remote.error.ApiExceptionResponse
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.request.PatchPlaygroundArrivalRequest
import com.happy.friendogly.remote.model.request.PatchPlaygroundMessageRequest
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
                onFailure = { throwable ->
                    when (throwable) {
                        is ApiExceptionResponse ->
                            DomainResult.Error(
                                throwable.error.data.errorCode.toData().toDomain(),
                            )

                        is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                    }
                },
            )
        }

        override suspend fun patchPlaygroundArrival(
            latitude: Double,
            longitude: Double,
        ): DomainResult<PlaygroundArrival, DataError.Network> {
            return source.patchPlaygroundArrival(
                PatchPlaygroundArrivalRequest(
                    latitude = latitude,
                    longitude = longitude,
                ),
            ).fold(
                onSuccess = { dto ->
                    DomainResult.Success(dto.toDomain())
                },
                onFailure = { throwable ->
                    when (throwable) {
                        is ApiExceptionResponse ->
                            DomainResult.Error(
                                throwable.error.data.errorCode.toData().toDomain(),
                            )

                        is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                    }
                },
            )
        }

        override suspend fun getPetExistence(): Result<PetExistence> =
            source.getPetExistence().mapCatching { dto ->
                dto.toDomain()
            }

        override suspend fun getPlaygrounds(
            startLatitude: Double,
            endLatitude: Double,
            startLongitude: Double,
            endLongitude: Double,
        ): Result<List<Playground>> =
            source
                .getPlaygrounds(startLatitude, endLatitude, startLongitude, endLongitude)
                .mapCatching { dto ->
                    dto.toDomain()
                }

        override suspend fun getMyPlayground(): DomainResult<MyPlayground, DataError.Network> =
            source.getMyPlayground().fold(
                onSuccess = { dto ->
                    DomainResult.Success(dto.toDomain())
                },
                onFailure = { throwable ->
                    when (throwable) {
                        is ApiExceptionResponse ->
                            DomainResult.Error(
                                throwable.error.data.errorCode.toData().toDomain(),
                            )

                        is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                    }
                },
            )

        override suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfo> =
            source
                .getPlaygroundInfo(id)
                .mapCatching { dto -> dto.toDomain() }

        override suspend fun getPlaygroundSummary(playgroundId: Long): Result<PlaygroundSummary> =
            source
                .getPlaygroundSummary(playgroundId)
                .mapCatching { dto -> dto.toDomain() }

        override suspend fun postPlaygroundJoin(playgroundId: Long): DomainResult<PlaygroundJoin, DataError.Network> {
            return source.postPlaygroundJoin(playgroundId).fold(
                onSuccess = { dto ->
                    DomainResult.Success(dto.toDomain())
                },
                onFailure = { throwable ->
                    when (throwable) {
                        is ApiExceptionResponse ->
                            DomainResult.Error(
                                throwable.error.data.errorCode.toData().toDomain(),
                            )

                        is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                    }
                },
            )
        }

        override suspend fun deletePlaygroundLeave(): Result<Unit> = source.deletePlaygroundLeave()

        override suspend fun patchPlaygroundMessage(message: String): Result<PlaygroundMessage> =
            source.patchPlaygroundMessage(
                PatchPlaygroundMessageRequest(message),
            ).mapCatching { dto ->
                dto.toDomain()
            }
    }
