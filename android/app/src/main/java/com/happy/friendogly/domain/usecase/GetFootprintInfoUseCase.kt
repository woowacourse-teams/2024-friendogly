package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.FootprintInfo
import com.happy.friendogly.domain.repository.FootprintRepository

class GetFootprintInfoUseCase(private val repository: FootprintRepository) {
    suspend operator fun invoke(footprintId: Long): Result<FootprintInfo> = repository.getFootprintInfo(footprintId)
}
