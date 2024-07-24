package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.Member
import com.woowacourse.friendogly.domain.repository.MemberRepository
import okhttp3.MultipartBody

class PostMemberUseCase(
    private val repository: MemberRepository,
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        file: MultipartBody.Part?,
    ): Result<Member> = repository.postMember(name = name, email = email, file = file)
}
