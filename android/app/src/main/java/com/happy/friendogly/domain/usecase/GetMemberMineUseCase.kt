package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.repository.MemberRepository

class GetMemberMineUseCase(
    private val repository: MemberRepository,
) {
    suspend operator fun invoke(): DomainResult<Member, DataError.Network> = repository.getMemberMine()
}
