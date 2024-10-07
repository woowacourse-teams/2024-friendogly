package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.MyFootprint
import javax.inject.Inject

class PostFootprintUseCase
    @Inject
    constructor(private val repository: WoofRepository) {
        suspend operator fun invoke(
            latitude: Double,
            longitude: Double,
        ): Result<MyFootprint> =
            repository.postFootprint(
                latitude,
                longitude,
            )
    }
