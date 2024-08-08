package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.repository.MemberRepository

class GetMemberUseCase(
    private val repository: MemberRepository,
) {
    suspend operator fun invoke(id: Long): Result<Member> = repository.getMember(id = id)
}
