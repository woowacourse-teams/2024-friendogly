package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.PetDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.ImageUpdateType
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.PetRepository
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class PetRepositoryImpl
    @Inject
    constructor(private val source: PetDataSource) : PetRepository {
        override suspend fun getPetsMine(): DomainResult<List<Pet>, DataError.Network> =
            source.getPetsMine().fold(
                onSuccess = { result ->
                    DomainResult.Success(result.map { petDto -> petDto.toDomain() })
                },
                onFailure = { e ->
                    when (e) {
                        is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                        is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                    }
                },
            )

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
                    when (e) {
                        is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                        is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                    }
                },
            )

        override suspend fun getPets(id: Long): DomainResult<List<Pet>, DataError.Network> =
            source.getPets(id = id).fold(
                onSuccess = { result ->
                    DomainResult.Success(result.map { petDto -> petDto.toDomain() })
                },
                onFailure = { e ->
                    when (e) {
                        is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                        is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                    }
                },
            )

        override suspend fun patchPet(
            petId: Long,
            name: String,
            description: String,
            birthDate: LocalDate,
            sizeType: SizeType,
            gender: Gender,
            file: MultipartBody.Part?,
            imageUpdateType: ImageUpdateType,
        ): DomainResult<Unit, DataError.Network> =
            source.patchPet(
                petId = petId,
                name = name,
                description = description,
                birthDate = birthDate,
                sizeType = sizeType.toData(),
                gender = gender.toData(),
                file = file,
                imageUpdateType = imageUpdateType.toData(),
            ).fold(
                onSuccess = {
                    DomainResult.Success(Unit)
                },
                onFailure = { e ->
                    when (e) {
                        is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                        is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                    }
                },
            )
    }
