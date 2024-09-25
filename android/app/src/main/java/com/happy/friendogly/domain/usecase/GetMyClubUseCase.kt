package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.repository.MyClubRepository
import javax.inject.Inject

class GetMyClubUseCase @Inject constructor(
    private val repository: MyClubRepository,
) {
    suspend operator fun invoke(): Result<List<Club>> = repository.getMyClubs()
}
