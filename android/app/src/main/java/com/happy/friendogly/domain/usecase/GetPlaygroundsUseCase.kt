package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.Playground
import javax.inject.Inject

class GetPlaygroundsUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(): Result<List<Playground>> = repository.getPlaygrounds()
    }
