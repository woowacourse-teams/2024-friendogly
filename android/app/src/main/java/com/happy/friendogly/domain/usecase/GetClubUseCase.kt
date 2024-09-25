package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.domain.repository.ClubRepository
import javax.inject.Inject

class GetClubUseCase
    @Inject
    constructor(
        private val repository: ClubRepository,
    ) {
        suspend operator fun invoke(id: Long): Result<ClubDetail> = repository.getClub(clubId = id)
    }
