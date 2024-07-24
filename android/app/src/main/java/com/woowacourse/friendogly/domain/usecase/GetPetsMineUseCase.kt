package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.Pet
import com.woowacourse.friendogly.domain.repository.PetRepository

class GetPetsMineUseCase(
    private val repository: PetRepository,
) {
    suspend operator fun invoke(): Result<List<Pet>> = repository.getPetsMine()
}
