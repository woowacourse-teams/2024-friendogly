package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.MemberDataSource
import com.woowacourse.friendogly.domain.model.Member
import com.woowacourse.friendogly.domain.repository.MemberRepository

class MemberRepositoryImpl(
    private val source: MemberDataSource,
) : MemberRepository {
    override suspend fun postMember(
        name: String,
        email: String,
    ): Result<Member> = source.postMember(name = name, email = email).mapCatching { result -> result.toDomain() }
}
