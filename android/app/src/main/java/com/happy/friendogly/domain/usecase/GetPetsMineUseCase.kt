package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.repository.PetRepository

class GetPetsMineUseCase(
    private val repository: PetRepository,
) {
    suspend operator fun invoke(): Result<List<Pet>> = repository.getPetsMine()
}
