package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.repository.MyClubRepository
import javax.inject.Inject

class GetMyHeadClubUseCase
    @Inject
    constructor(
        private val repository: MyClubRepository,
    ) {
        suspend operator fun invoke(): DomainResult<List<Club>, DataError.Network> = repository.getMyHeadClubs()
    }
