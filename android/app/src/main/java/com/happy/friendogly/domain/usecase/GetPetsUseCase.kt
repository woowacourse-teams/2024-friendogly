package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.repository.PetRepository

class GetPetsUseCase(
    private val repository: PetRepository,
) {
    suspend operator fun invoke(id: Long): Result<List<Pet>> = repository.getPets(id = id)
}
