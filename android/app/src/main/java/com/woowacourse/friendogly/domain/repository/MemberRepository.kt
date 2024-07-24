package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.Member

interface MemberRepository {
    suspend fun postMember(
        name: String,
        email: String,
    ): Result<Member>

    suspend fun getMemberMine(): Result<Member>
}
