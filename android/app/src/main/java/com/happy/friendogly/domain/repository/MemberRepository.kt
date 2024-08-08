package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.model.Register
import okhttp3.MultipartBody

interface MemberRepository {
    suspend fun postMember(
        name: String,
        email: String,
        accessToken: String,
        file: MultipartBody.Part?,
    ): Result<Register>

    suspend fun getMemberMine(): Result<Member>

    suspend fun getMember(id: Long): Result<Member>
}
