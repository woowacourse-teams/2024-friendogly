package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.ClubRepository

class PostClubParticipationUseCase(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.postClubParticipation()
}
