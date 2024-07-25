package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.Member
import com.woowacourse.friendogly.domain.repository.MemberRepository

class GetMemberMineUseCase(
    private val repository: MemberRepository,
) {
    suspend operator fun invoke(): Result<Member> = repository.getMemberMine()
}
