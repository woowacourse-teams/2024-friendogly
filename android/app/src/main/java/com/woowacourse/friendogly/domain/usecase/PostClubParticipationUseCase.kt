package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.repository.ClubRepository

class PostClubParticipationUseCase(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.postClubParticipation()
}
