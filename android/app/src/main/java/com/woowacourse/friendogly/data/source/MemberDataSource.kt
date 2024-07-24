package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.MemberDto

interface MemberDataSource {
    suspend fun postMember(
        name: String,
        email: String,
    ): Result<MemberDto>

    suspend fun getMemberMine(): Result<MemberDto>
}
