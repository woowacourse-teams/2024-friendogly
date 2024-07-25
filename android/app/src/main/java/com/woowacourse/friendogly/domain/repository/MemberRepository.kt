package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.Member
import okhttp3.MultipartBody

interface MemberRepository {
    suspend fun postMember(
        name: String,
        email: String,
        file: MultipartBody.Part?,
    ): Result<Member>

    suspend fun getMemberMine(): Result<Member>
}
