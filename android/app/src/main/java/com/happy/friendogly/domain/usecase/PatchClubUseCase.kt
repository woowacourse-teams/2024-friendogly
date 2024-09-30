package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.domain.repository.ClubRepository
import javax.inject.Inject

class PatchClubUseCase
    @Inject
    constructor(private val repository: ClubRepository) {
        suspend operator fun invoke(
            clubId: Long,
            title: String,
            content: String,
            state: ClubState,
        ): DomainResult<Unit, DataError.Network> =
            repository.patchClub(
                clubId = clubId,
                title = title,
                content = content,
                state = state,
            )
    }
