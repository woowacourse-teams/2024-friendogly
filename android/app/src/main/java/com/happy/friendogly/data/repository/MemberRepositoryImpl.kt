package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.MemberDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.model.Register
import com.happy.friendogly.domain.repository.MemberRepository
import okhttp3.MultipartBody

class MemberRepositoryImpl(
    private val source: MemberDataSource,
) : MemberRepository {
    override suspend fun postMember(
        name: String,
        accessToken: String,
        file: MultipartBody.Part?,
    ): DomainResult<Register, DataError.Network> =
        source.postMember(name = name, accessToken = accessToken, file = file).fold(
            onSuccess = { registerDto ->
                DomainResult.Success(registerDto.toDomain())
            },
            onFailure = { e ->
                if (e is ApiExceptionDto) {
                    DomainResult.Error(e.error.data.errorCode.toDomain())
                } else {
                    DomainResult.Error(DataError.Network.NO_INTERNET)
                }
            },
        )

    override suspend fun getMemberMine(): Result<Member> = source.getMemberMine().mapCatching { result -> result.toDomain() }

    override suspend fun getMember(id: Long): Result<Member> = source.getMember(id = id).mapCatching { result -> result.toDomain() }
}
