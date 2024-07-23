package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.Member
import com.woowacourse.friendogly.domain.repository.MemberRepository

class PostMemberUseCase(
    private val repository: MemberRepository,
) {
    suspend operator fun invoke(
        name: String,
        email: String,
    ): Result<Member> = repository.postMember(name = name, email = email)
}
