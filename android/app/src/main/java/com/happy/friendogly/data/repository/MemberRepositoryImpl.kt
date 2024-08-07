package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.MemberDataSource
import com.happy.friendogly.domain.mapper.toDomain
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.model.Register
import com.happy.friendogly.domain.repository.MemberRepository
import okhttp3.MultipartBody

class MemberRepositoryImpl(
    private val source: MemberDataSource,
) : MemberRepository {
    override suspend fun postMember(
        name: String,
        email: String,
        accessToken: String,
        file: MultipartBody.Part?,
    ): Result<Register> =
        source.postMember(name = name, email = email, accessToken = accessToken, file = file)
            .mapCatching { result -> result.toDomain() }

    override suspend fun getMemberMine(): Result<Member> = source.getMemberMine().mapCatching { result -> result.toDomain() }
}
