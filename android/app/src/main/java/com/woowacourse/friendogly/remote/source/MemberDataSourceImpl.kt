package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.MemberDto
import com.woowacourse.friendogly.data.source.MemberDataSource
import com.woowacourse.friendogly.remote.api.MemberService
import com.woowacourse.friendogly.remote.mapper.toData
import com.woowacourse.friendogly.remote.model.request.PostMembersRequest

class MemberDataSourceImpl(
    private val service: MemberService,
) : MemberDataSource {
    override suspend fun postMember(
        name: String,
        email: String,
    ): Result<MemberDto> =
        runCatching {
            val body = PostMembersRequest(name = name, email = email)
            service.postMember(body = body).toData()
        }
}
