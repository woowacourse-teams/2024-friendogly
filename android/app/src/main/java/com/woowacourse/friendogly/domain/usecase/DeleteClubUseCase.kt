package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.Location
import com.woowacourse.friendogly.domain.repository.ClubRepository

class DeleteClubUseCase(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(id: Long): Result<Location> = repository.deleteClub(id = id)
}
