package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.ClubRepository
import javax.inject.Inject

class PostClubMemberUseCase @Inject constructor(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(
        id: Long,
        participatingPetsId: List<Long>,
    ) = repository.postClubMember(
        clubId = id,
        participatingPetsId = participatingPetsId,
    )
}
