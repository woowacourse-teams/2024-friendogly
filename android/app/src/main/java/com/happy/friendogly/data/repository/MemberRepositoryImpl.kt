package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.MemberDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.ImageUpdateType
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.model.Register
import com.happy.friendogly.domain.repository.MemberRepository
import okhttp3.MultipartBody
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
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
                when (e) {
                    is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                    is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                }
            },
        )

    override suspend fun getMemberMine(): DomainResult<Member, DataError.Network> =
        source.getMemberMine().fold(
            onSuccess = { memberDto ->
                DomainResult.Success(memberDto.toDomain())
            },
            onFailure = { e ->
                when (e) {
                    is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                    is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                }
            },
        )

    override suspend fun getMember(id: Long): DomainResult<Member, DataError.Network> =
        source.getMember(id = id).fold(
            onSuccess = { memberDto ->
                DomainResult.Success(memberDto.toDomain())
            },
            onFailure = { e ->
                when (e) {
                    is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                    is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                }
            },
        )

    override suspend fun patchMember(
        name: String,
        imageUpdateType: ImageUpdateType,
        file: MultipartBody.Part?,
    ): DomainResult<Member, DataError.Network> =
        source.patchMember(
            name = name,
            imageUpdateType = imageUpdateType.toData(),
            file = file,
        ).fold(
            onSuccess = { memberDto ->
                DomainResult.Success(memberDto.toDomain())
            },
            onFailure = { e ->
                when (e) {
                    is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                    is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                }
            },
        )

    override suspend fun deleteMember(): DomainResult<Unit, DataError.Network> =
        source.deleteMember().fold(
            onSuccess = {
                DomainResult.Success(Unit)
            },
            onFailure = { e ->
                when (e) {
                    is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                    is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                }
            },
        )
}
