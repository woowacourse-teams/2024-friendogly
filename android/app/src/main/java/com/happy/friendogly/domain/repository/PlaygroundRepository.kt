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

    suspend fun getPetExistence(): DomainResult<PetExistence, DataError.Network>

    suspend fun getPlaygrounds(
        startLatitude: Double,
        endLatitude: Double,
        startLongitude: Double,
        endLongitude: Double,
    ): DomainResult<List<Playground>, DataError.Network>

    suspend fun getMyPlayground(): DomainResult<MyPlayground, DataError.Network>

    suspend fun getPlaygroundInfo(id: Long): DomainResult<PlaygroundInfo, DataError.Network>

    suspend fun getPlaygroundSummary(playgroundId: Long): DomainResult<PlaygroundSummary, DataError.Network>

    suspend fun postPlaygroundJoin(playgroundId: Long): DomainResult<PlaygroundJoin, DataError.Network>

    suspend fun deletePlaygroundLeave(): DomainResult<Unit, DataError.Network>

    suspend fun patchPlaygroundMessage(message: String): DomainResult<PlaygroundMessage, DataError.Network>
}
