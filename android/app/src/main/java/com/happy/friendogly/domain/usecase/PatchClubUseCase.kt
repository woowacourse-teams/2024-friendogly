package com.happy.friendogly.domain.usecase

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
        ): Result<Unit> =
            repository.patchClub(
                clubId = clubId,
                title = title,
                content = content,
                state = state,
            )
    }
