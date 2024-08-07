package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.Member
import okhttp3.MultipartBody

interface MemberRepository {
    suspend fun postMember(
        name: String,
        email: String,
        file: MultipartBody.Part?,
    ): Result<Member>

    suspend fun getMemberMine(): Result<Member>

    suspend fun getMember(id: Long): Result<Member>
}
