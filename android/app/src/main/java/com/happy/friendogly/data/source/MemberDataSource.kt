package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.MemberDto
import okhttp3.MultipartBody

interface MemberDataSource {
    suspend fun postMember(
        name: String,
        email: String,
        file: MultipartBody.Part?,
    ): Result<MemberDto>

    suspend fun getMemberMine(): Result<MemberDto>

    suspend fun getMember(id: Long): Result<MemberDto>
}
