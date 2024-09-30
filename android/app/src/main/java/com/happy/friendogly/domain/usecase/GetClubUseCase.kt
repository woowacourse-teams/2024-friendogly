package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.domain.repository.ClubRepository
import javax.inject.Inject

class GetClubUseCase
    @Inject
    constructor(
        private val repository: ClubRepository,
    ) {
        suspend operator fun invoke(id: Long): DomainResult<ClubDetail, DataError.Network> = repository.getClub(clubId = id)
    }
