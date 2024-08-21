package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.model.Register
import okhttp3.MultipartBody

interface MemberRepository {
    suspend fun postMember(
        name: String,
        accessToken: String,
        file: MultipartBody.Part?,
    ): DomainResult<Register, DataError.Network>

    suspend fun getMemberMine(): DomainResult<Member, DataError.Network>

    suspend fun getMember(id: Long): DomainResult<Member, DataError.Network>

    suspend fun deleteMember(): DomainResult<Unit, DataError.Network>
}
