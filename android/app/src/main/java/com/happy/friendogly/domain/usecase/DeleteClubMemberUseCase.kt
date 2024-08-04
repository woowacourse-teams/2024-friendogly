package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.ClubRepository

class DeleteClubMemberUseCase(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(id: Long): Result<Unit> =
        repository.deleteClubMember(clubId = id)
}
