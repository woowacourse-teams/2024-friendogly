package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.MemberDto
import com.happy.friendogly.data.model.RegisterDto
import okhttp3.MultipartBody

interface MemberDataSource {
    suspend fun postMember(
        name: String,
        email: String,
        accessToken: String,
        file: MultipartBody.Part?,
    ): Result<RegisterDto>

    suspend fun getMemberMine(): Result<MemberDto>
}
