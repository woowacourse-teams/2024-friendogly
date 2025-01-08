package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Register
import com.happy.friendogly.domain.repository.MemberRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostMemberUseCase
    @Inject
    constructor(
        private val repository: MemberRepository,
    ) {
        suspend operator fun invoke(
            name: String,
            accessToken: String,
            file: MultipartBody.Part?,
        ): DomainResult<Register, DataError.Network> = repository.postMember(name = name, accessToken = accessToken, file = file)
    }
