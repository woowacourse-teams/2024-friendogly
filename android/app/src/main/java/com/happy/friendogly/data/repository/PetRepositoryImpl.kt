package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.PetDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
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
    ): DomainResult<Pet, DataError.Network> =
        source.postPet(
            name = name,
            description = description,
            birthday = birthday,
            sizeType = sizeType.toData(),
            gender = gender.toData(),
            file = file,
        ).fold(
            onSuccess = { petDto ->
                DomainResult.Success(petDto.toDomain())
            },
            onFailure = { e ->
                if (e is ApiExceptionDto) {
                    DomainResult.Error(e.error.data.errorCode.toDomain())
                } else {
                    DomainResult.Error(DataError.Network.SERVER_ERROR)
                }
            },
        )

    override suspend fun getPets(id: Long): Result<List<Pet>> =
        source.getPets(id = id).mapCatching { result -> result.map { petDto -> petDto.toDomain() } }
}
