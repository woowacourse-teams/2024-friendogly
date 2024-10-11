package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.MyPlayground
import javax.inject.Inject

class PostPlaygroundUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(
            latitude: Double,
            longitude: Double,
        ): Result<MyPlayground> =
            repository.postPlayground(
                latitude,
                longitude,
            )
    }
