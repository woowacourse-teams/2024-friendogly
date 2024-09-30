package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.RecentPet
import com.happy.friendogly.domain.repository.RecentPetsRepository
import javax.inject.Inject

class GetAllRecentPetUseCase
    @Inject
    constructor(
        val repository: RecentPetsRepository,
    ) {
        suspend operator fun invoke(): DomainResult<List<RecentPet>, DataError.Local> = repository.getAllRecentPet()
    }
