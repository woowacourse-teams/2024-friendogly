package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.MemberDto
import com.happy.friendogly.data.source.MemberDataSource
import com.happy.friendogly.remote.api.MemberService
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
    ): Result<MemberDto> =
        runCatching {
            val body = PostMembersRequest(name = name, email = email)
            service.postMember(body = body, file = file).data.toData()
        }

    override suspend fun getMemberMine(): Result<MemberDto> =
        runCatching {
            service.getMemberMine().data.toData()
        }
}