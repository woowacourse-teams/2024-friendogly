package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Location
import com.happy.friendogly.domain.repository.ClubRepository

class PostClubUseCase(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(): Result<Location> = repository.postClub()
}
