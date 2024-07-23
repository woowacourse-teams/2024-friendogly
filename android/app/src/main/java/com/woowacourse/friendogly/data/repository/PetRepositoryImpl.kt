package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toData
import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.PetDataSource
import com.woowacourse.friendogly.domain.model.Gender
import com.woowacourse.friendogly.domain.model.Pet
import com.woowacourse.friendogly.domain.model.SizeType
import com.woowacourse.friendogly.domain.repository.PetRepository
import kotlinx.datetime.LocalDate

class PetRepositoryImpl(private val source: PetDataSource) : PetRepository {
    override suspend fun getPetsMine(): Result<List<Pet>> =
        source.getPetsMine().mapCatching { result -> result.map { petDto -> petDto.toDomain() } }

    override suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeType,
        gender: Gender,
        imageUrl: String,
    ): Result<Pet> =
        source.postPet(
            name = name,
            description = description,
            birthday = birthday,
            sizeType = sizeType.toData(),
            gender = gender.toData(),
            imageUrl = imageUrl,
        ).mapCatching { result -> result.toDomain() }
}
