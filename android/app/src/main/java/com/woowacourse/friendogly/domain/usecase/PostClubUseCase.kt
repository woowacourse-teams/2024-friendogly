package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.Location
import com.woowacourse.friendogly.domain.repository.ClubRepository

class PostClubUseCase(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(): Result<Location> = repository.postClub()
}
