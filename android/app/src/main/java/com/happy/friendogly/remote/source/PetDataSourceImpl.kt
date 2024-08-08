package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.data.model.PetDto
import com.happy.friendogly.data.model.SizeTypeDto
import com.happy.friendogly.data.source.PetDataSource
import com.happy.friendogly.remote.api.PetService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.mapper.toRemote
import com.happy.friendogly.remote.model.request.PostPetRequest
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

class PetDataSourceImpl(private val service: PetService) : PetDataSource {
    override suspend fun getPetsMine(): Result<List<PetDto>> =
        runCatching {
            service.getPetsMine().data.map { petResponse -> petResponse.toData() }
        }

    override suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeTypeDto,
        gender: GenderDto,
        file: MultipartBody.Part?,
    ): Result<PetDto> =
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

    override suspend fun getPets(id: Long): Result<List<PetDto>> =
        runCatching {
            service.getPets(id = id).data.map { petResponse -> petResponse.toData() }
        }
}
