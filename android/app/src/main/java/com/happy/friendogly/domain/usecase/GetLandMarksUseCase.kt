package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.LandMark
import com.happy.friendogly.domain.repository.WoofRepository

class GetLandMarksUseCase(private val repository: WoofRepository) {
    suspend operator fun invoke(): Result<List<LandMark>> = repository.getLandMarks()
}
