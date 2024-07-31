package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.FootprintRepository
import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo

class GetFootprintInfoUseCase(private val repository: FootprintRepository) {
    suspend operator fun invoke(footprintId: Long): Result<FootprintInfo> = repository.getFootprintInfo(footprintId)
}
