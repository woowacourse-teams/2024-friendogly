package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.data.model.PetDto
import com.happy.friendogly.data.model.SizeTypeDto
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

interface PetDataSource {
    suspend fun getPetsMine(): Result<List<PetDto>>

    suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeTypeDto,
        gender: GenderDto,
        file: MultipartBody.Part?,
    ): Result<PetDto>

    suspend fun getPets(id: Long): Result<List<PetDto>>
}
