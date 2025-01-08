package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.Playground
import javax.inject.Inject

class GetPlaygroundsUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(
            startLatitude: Double,
            endLatitude: Double,
            startLongitude: Double,
            endLongitude: Double,
        ): DomainResult<List<Playground>, DataError.Network> =
            repository.getPlaygrounds(startLatitude, endLatitude, startLongitude, endLongitude)
    }
