package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.ClubRepository
import javax.inject.Inject

class DeleteClubMemberUseCase
    @Inject
    constructor(
        private val repository: ClubRepository,
    ) {
        suspend operator fun invoke(id: Long): DomainResult<Unit,DataError.Network> = repository.deleteClubMember(clubId = id)
    }
