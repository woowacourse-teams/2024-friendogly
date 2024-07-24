package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.MemberDataSource
import com.woowacourse.friendogly.domain.model.Member
import com.woowacourse.friendogly.domain.repository.MemberRepository
import okhttp3.MultipartBody

class MemberRepositoryImpl(
    private val source: MemberDataSource,
) : MemberRepository {
    override suspend fun postMember(
        name: String,
        email: String,
        file: MultipartBody.Part?,
    ): Result<Member> =
        source.postMember(name = name, email = email, file = file)
            .mapCatching { result -> result.toDomain() }
}
