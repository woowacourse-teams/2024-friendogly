package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.GenderDto
import com.woowacourse.friendogly.data.model.PetDto
import com.woowacourse.friendogly.data.model.SizeTypeDto
import com.woowacourse.friendogly.data.source.PetDataSource
import com.woowacourse.friendogly.remote.api.PetService
import com.woowacourse.friendogly.remote.mapper.toData
import com.woowacourse.friendogly.remote.mapper.toRemote
import com.woowacourse.friendogly.remote.model.request.PostPetRequest
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
}
