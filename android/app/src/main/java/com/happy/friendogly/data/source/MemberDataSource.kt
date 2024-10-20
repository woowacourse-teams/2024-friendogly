package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.ImageUpdateTypeDto
import com.happy.friendogly.data.model.MemberDto
import com.happy.friendogly.data.model.RegisterDto
import okhttp3.MultipartBody

interface MemberDataSource {
    suspend fun postMember(
        name: String,
        accessToken: String,
        file: MultipartBody.Part?,
    ): Result<RegisterDto>

    suspend fun getMemberMine(): Result<MemberDto>

    suspend fun getMember(id: Long): Result<MemberDto>

    suspend fun patchMember(
        name: String,
        imageUpdateType: ImageUpdateTypeDto,
        file: MultipartBody.Part?,
    ): Result<MemberDto>

    suspend fun deleteMember(): Result<Unit>
}
