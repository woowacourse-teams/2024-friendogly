package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.domain.repository.ClubRepository

class GetClubUseCase(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(id: Long): Result<ClubDetail> = repository.getClub(clubId = id)
}
