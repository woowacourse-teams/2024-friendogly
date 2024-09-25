package com.happy.friendogly.remote.source

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.data.model.ImageUpdateTypeDto
import com.happy.friendogly.data.model.PetDto
import com.happy.friendogly.data.model.SizeTypeDto
import com.happy.friendogly.data.source.PetDataSource
import com.happy.friendogly.remote.api.PetService
import com.happy.friendogly.remote.error.ApiExceptionResponse
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.mapper.toRemote
import com.happy.friendogly.remote.model.request.PatchPetRequest
import com.happy.friendogly.remote.model.request.PostPetRequest
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody
import javax.inject.Inject

class PetDataSourceImpl @Inject constructor(private val service: PetService) : PetDataSource {
    override suspend fun getPetsMine(): Result<List<PetDto>> {
        val result =
            runCatching {
                service.getPetsMine().data.map { petResponse -> petResponse.toData() }
            }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is ApiExceptionResponse -> Result.failure(exception.toData())
            else -> Result.failure(exception)
        }
    }

    override suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeTypeDto,
        gender: GenderDto,
        file: MultipartBody.Part?,
    ): Result<PetDto> {
        val result =
            runCatching {
                val body =
                    PostPetRequest(
                        name = name,
                        description = description,
                        birthDate = birthday,
                        sizeType = sizeType.toRemote(),
                        gender = gender.toRemote(),
                    )

                service.postPet(body = body, file = file).data.toData()
            }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is ApiExceptionResponse -> Result.failure(exception.toData())
            is IllegalStateException -> Result.failure(ApiExceptionDto.FileSizeExceedExceptionDto)
            else -> Result.failure(exception)
        }
    }

    override suspend fun getPets(id: Long): Result<List<PetDto>> {
        val result =
            runCatching {
                service.getPets(id = id).data.map { petResponse -> petResponse.toData() }
            }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is ApiExceptionResponse -> Result.failure(exception.toData())
            else -> Result.failure(exception)
        }
    }

    override suspend fun patchPet(
        petId: Long,
        name: String,
        description: String,
        birthDate: LocalDate,
        sizeType: SizeTypeDto,
        gender: GenderDto,
        file: MultipartBody.Part?,
        imageUpdateType: ImageUpdateTypeDto,
    ): Result<Unit> {
        val result =
            runCatching {
                val body =
                    PatchPetRequest(
                        name = name,
                        description = description,
                        birthDate = birthDate,
                        sizeType = sizeType.toRemote(),
                        gender = gender.toRemote(),
                        imageUpdateType = imageUpdateType.toRemote(),
                    )

                service.patchPet(petId = petId, body = body, file = file)
            }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is ApiExceptionResponse -> Result.failure(exception.toData())
            is IllegalStateException -> Result.failure(ApiExceptionDto.FileSizeExceedExceptionDto)
            else -> Result.failure(exception)
        }
    }
}
