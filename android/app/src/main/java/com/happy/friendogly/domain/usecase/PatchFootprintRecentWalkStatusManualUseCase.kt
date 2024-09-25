package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import javax.inject.Inject

class PatchFootprintRecentWalkStatusManualUseCase
    @Inject
    constructor(private val repository: WoofRepository) {
        suspend operator fun invoke(walkStatus: WalkStatus): Result<FootprintRecentWalkStatus> =
            repository.patchFootprintRecentWalkStatusManual(walkStatus = walkStatus)
    }
