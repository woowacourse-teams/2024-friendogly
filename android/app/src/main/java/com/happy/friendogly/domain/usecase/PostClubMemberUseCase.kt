package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.ClubRepository

class PostClubMemberUseCase(
    private val repository: ClubRepository
) {
    suspend operator fun invoke(
        id: Long,
        participatingPetsId: List<Long>,
    ) = repository.postClubMember(
        clubId = id,
        participatingPetsId = participatingPetsId
    )
}
