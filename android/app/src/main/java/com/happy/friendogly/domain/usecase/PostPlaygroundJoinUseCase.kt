package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundJoin
import javax.inject.Inject

class PostPlaygroundJoinUseCase
    @Inject
    constructor(private val repository: WoofRepository) {
        suspend operator fun invoke(playgroundId: Long): Result<PlaygroundJoin> = repository.postPlaygroundJoin(playgroundId)
    }
