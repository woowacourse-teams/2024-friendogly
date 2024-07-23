package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.Gender
import com.woowacourse.friendogly.domain.model.Pet
import com.woowacourse.friendogly.domain.model.SizeType
import kotlinx.datetime.LocalDate

interface PetRepository {
    suspend fun getPetsMine(): Result<List<Pet>>

    suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeType,
        gender: Gender,
        imageUrl: String,
    ): Result<Pet>
}
