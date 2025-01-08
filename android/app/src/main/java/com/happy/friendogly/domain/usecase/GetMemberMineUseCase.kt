package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.repository.MemberRepository
import javax.inject.Inject

class GetMemberMineUseCase
    @Inject
    constructor(
        private val repository: MemberRepository,
    ) {
        suspend operator fun invoke(): DomainResult<Member, DataError.Network> = repository.getMemberMine()
    }
