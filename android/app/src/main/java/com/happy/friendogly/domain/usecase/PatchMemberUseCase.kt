package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.ImageUpdateType
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.repository.MemberRepository
import okhttp3.MultipartBody

class PatchMemberUseCase(private val repository: MemberRepository) {
    suspend operator fun invoke(
        name: String,
        imageUpdateType: ImageUpdateType,
        file: MultipartBody.Part?,
    ): DomainResult<Member, DataError.Network> =
        repository.patchMember(
            name = name,
            imageUpdateType = imageUpdateType,
            file = file,
        )
}
