package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.repository.ClubRepository

class GetClubMineUseCase(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.getClubMine()
}
