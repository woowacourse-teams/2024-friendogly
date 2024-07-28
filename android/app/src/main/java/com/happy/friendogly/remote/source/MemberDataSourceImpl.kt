package com.happy.friendogly.remote.source

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.model.MemberDto
import com.happy.friendogly.data.source.MemberDataSource
import com.happy.friendogly.remote.api.MemberService
import com.happy.friendogly.remote.error.ApiExceptionResponse
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.request.PostMembersRequest
import okhttp3.MultipartBody

class MemberDataSourceImpl(
    private val service: MemberService,
) : MemberDataSource {
    override suspend fun postMember(
        name: String,
        email: String,
        file: MultipartBody.Part?,
    ): Result<MemberDto> {
        val body = PostMembersRequest(name = name, email = email)
        val result = runCatching { service.postMember(body = body, file = file).data.toData() }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is ApiExceptionResponse -> Result.failure(exception.toData())
            else -> throw exception
        }
    }

    override suspend fun getMemberMine(): Result<MemberDto> =
        runCatching {
            service.getMemberMine().data.toData()
        }
}
