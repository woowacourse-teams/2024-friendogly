package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.RecentPetsRepository
import javax.inject.Inject

class InsertRecentPetUseCase
    @Inject
    constructor(
        val repository: RecentPetsRepository,
    ) {
        suspend operator fun invoke(
            imgUrl: String,
            name: String,
            id: Long,
        ): DomainResult<Unit, DataError.Local> = repository.insertRecentPet(imgUrl = imgUrl, name = name, id = id)
    }
