package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.PetExistence
import javax.inject.Inject

class GetPetExistenceUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(): Result<PetExistence> = repository.getPetExistence()
    }
