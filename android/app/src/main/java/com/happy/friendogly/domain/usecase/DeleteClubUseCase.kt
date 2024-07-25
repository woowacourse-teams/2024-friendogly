package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Location
import com.happy.friendogly.domain.repository.ClubRepository

class DeleteClubUseCase(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(id: Long): Result<Location> = repository.deleteClub(id = id)
}
