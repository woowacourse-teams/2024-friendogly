package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.ImageUpdateType
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

interface PetRepository {
    suspend fun getPetsMine(): DomainResult<List<Pet>, DataError.Network>

    suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeType,
        gender: Gender,
        file: MultipartBody.Part?,
    ): DomainResult<Pet, DataError.Network>

    suspend fun getPets(id: Long): DomainResult<List<Pet>, DataError.Network>

    suspend fun patchPet(
        petId: Long,
        name: String,
        description: String,
        birthDate: LocalDate,
        sizeType: SizeType,
        gender: Gender,
        file: MultipartBody.Part?,
        imageUpdateType: ImageUpdateType,
    ): DomainResult<Unit, DataError.Network>
}
