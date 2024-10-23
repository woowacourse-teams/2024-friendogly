package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.RecentPet
import com.happy.friendogly.domain.repository.RecentPetsRepository
import javax.inject.Inject

class GetRecentPetByIdUseCase
    @Inject
    constructor(
        val repository: RecentPetsRepository,
    ) {
        suspend operator fun invoke(id: Long): DomainResult<RecentPet, DataError.Local> = repository.getRecentPetById(id = id)
    }
