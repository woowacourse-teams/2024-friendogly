package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository

class DeleteFootprintUseCase(private val repository: WoofRepository) {
    suspend operator fun invoke(footprintId: Long): Result<Unit> = repository.deleteFootprint(footprintId = footprintId)
}
