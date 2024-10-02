package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.Playground
import javax.inject.Inject

class GetPlaygroundsUseCase
    @Inject
    constructor(private val repository: WoofRepository) {
        suspend operator fun invoke(): Result<List<Playground>> = repository.getPlaygrounds()
    }
