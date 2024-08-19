package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus

class PatchFootprintRecentWalkStatusAutoUseCase(private val repository: WoofRepository) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): Result<FootprintRecentWalkStatus> = repository.patchFootprintRecentWalkStatusAuto(latitude = latitude, longitude = longitude)
}
