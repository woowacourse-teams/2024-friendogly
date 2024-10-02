package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo
import javax.inject.Inject

class GetPlaygroundInfoUseCase
    @Inject
    constructor(private val repository: WoofRepository) {
        suspend operator fun invoke(footprintId: Long): Result<PlaygroundInfo> = repository.getPlaygroundInfo(footprintId)
    }
