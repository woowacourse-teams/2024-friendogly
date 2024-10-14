package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.presentation.ui.playground.model.MyPlayground
import com.happy.friendogly.presentation.ui.playground.model.PetExistence
import com.happy.friendogly.presentation.ui.playground.model.Playground
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundArrival
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundJoin
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundSummary

interface PlaygroundRepository {
    suspend fun postPlayground(
        latitude: Double,
        longitude: Double,
    ): DomainResult<MyPlayground, DataError.Network>

    suspend fun patchPlaygroundArrival(
        latitude: Double,
        longitude: Double,
    ): Result<PlaygroundArrival>

    suspend fun getFootprintMarkBtnInfo(): Result<PetExistence>

    suspend fun getPlaygrounds(): Result<List<Playground>>

    suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfo>

    suspend fun getPlaygroundSummary(playgroundId: Long): Result<PlaygroundSummary>

    suspend fun postPlaygroundJoin(playgroundId: Long): Result<PlaygroundJoin>

    suspend fun deletePlaygroundLeave(): Result<Unit>
}
