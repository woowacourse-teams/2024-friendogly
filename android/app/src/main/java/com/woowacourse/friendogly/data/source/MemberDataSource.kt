package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.MemberDto
import okhttp3.MultipartBody

interface MemberDataSource {
    suspend fun postMember(
        name: String,
        email: String,
        file: MultipartBody.Part?,
    ): Result<MemberDto>

    suspend fun getMemberMine(): Result<MemberDto>
}
