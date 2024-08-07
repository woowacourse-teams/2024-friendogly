package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.PetDataSource
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.PetRepository
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

class PetRepositoryImpl(private val source: PetDataSource) : PetRepository {
    override suspend fun getPetsMine(): Result<List<Pet>> =
        source.getPetsMine().mapCatching { result -> result.map { petDto -> petDto.toDomain() } }

    override suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeType,
        gender: Gender,
        file: MultipartBody.Part?,
    ): Result<Pet> =
        source.postPet(
            name = name,
            description = description,
            birthday = birthday,
            sizeType = sizeType.toData(),
            gender = gender.toData(),
            file = file,
        ).mapCatching { result -> result.toDomain() }

    override suspend fun getPets(id: Long): Result<List<Pet>> =
        source.getPets(id = id).mapCatching { result -> result.map { petDto -> petDto.toDomain() } }
}
