package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.Gender
import com.woowacourse.friendogly.domain.model.Pet
import com.woowacourse.friendogly.domain.model.SizeType
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

interface PetRepository {
    suspend fun getPetsMine(): Result<List<Pet>>

    suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeType,
        gender: Gender,
        file: MultipartBody.Part?,
    ): Result<Pet>
}
