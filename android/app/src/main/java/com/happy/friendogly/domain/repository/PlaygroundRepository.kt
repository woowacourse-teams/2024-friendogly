package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.presentation.ui.playground.model.MyPlayground
import com.happy.friendogly.presentation.ui.playground.model.PetExistence
import com.happy.friendogly.presentation.ui.playground.model.Playground
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundArrival
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundJoin
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundMessage
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundSummary

interface PlaygroundRepository {
    suspend fun postPlayground(
        latitude: Double,
        longitude: Double,
    ): DomainResult<MyPlayground, DataError.Network>

    suspend fun patchPlaygroundArrival(
        latitude: Double,
        longitude: Double,
    ): DomainResult<PlaygroundArrival, DataError.Network>

    suspend fun getPetExistence(): Result<PetExistence>

    suspend fun getPlaygrounds(
        startLatitude: Double,
        endLatitude: Double,
        startLongitude: Double,
        endLongitude: Double,
    ): Result<List<Playground>>

    suspend fun getMyPlayground(): DomainResult<MyPlayground, DataError.Network>

    suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfo>

    suspend fun getPlaygroundSummary(playgroundId: Long): Result<PlaygroundSummary>

    suspend fun postPlaygroundJoin(playgroundId: Long): DomainResult<PlaygroundJoin, DataError.Network>

    suspend fun deletePlaygroundLeave(): Result<Unit>

    suspend fun patchPlaygroundMessage(message: String): Result<PlaygroundMessage>
}
