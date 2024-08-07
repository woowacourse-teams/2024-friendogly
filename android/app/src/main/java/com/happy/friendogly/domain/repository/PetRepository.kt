package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
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

    suspend fun getPets(id: Long): Result<List<Pet>>
}
