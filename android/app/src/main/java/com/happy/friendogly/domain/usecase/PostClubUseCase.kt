package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.ClubRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostClubUseCase @Inject constructor(
    private val repository: ClubRepository,
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        address: ClubAddress,
        allowedGender: List<Gender>,
        allowedSize: List<SizeType>,
        memberCapacity: Int,
        file: MultipartBody.Part?,
        petIds: List<Long>,
    ): Result<Unit> =
        repository.postClub(
            title = title,
            content = content,
            address = address,
            allowedGender = allowedGender,
            allowedSize = allowedSize,
            memberCapacity = memberCapacity,
            file = file,
            petIds = petIds,
        )
}
