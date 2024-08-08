package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Register
import com.happy.friendogly.domain.repository.MemberRepository
import okhttp3.MultipartBody

class PostMemberUseCase(
    private val repository: MemberRepository,
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        accessToken: String,
        file: MultipartBody.Part?,
    ): Result<Register> = repository.postMember(name = name, email = email, accessToken = accessToken, file = file)
}
