package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.PetExistence
import javax.inject.Inject

class GetPetExistenceUseCase
    @Inject
    constructor(private val repository: WoofRepository) {
        suspend operator fun invoke(): Result<PetExistence> = repository.getFootprintMarkBtnInfo()
    }
