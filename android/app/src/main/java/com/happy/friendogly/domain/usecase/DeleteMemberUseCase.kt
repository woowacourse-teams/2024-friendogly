package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.MemberRepository

class DeleteMemberUseCase(
    private val repository: MemberRepository,
) {
    suspend operator fun invoke(): DomainResult<Unit, DataError.Network> = repository.deleteMember()
}