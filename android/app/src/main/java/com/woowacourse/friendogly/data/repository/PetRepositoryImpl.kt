package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.PetDataSource
import com.woowacourse.friendogly.domain.model.Pet
import com.woowacourse.friendogly.domain.repository.PetRepository

class PetRepositoryImpl(private val source: PetDataSource) : PetRepository {
    override suspend fun getPetsMine(): Result<List<Pet>> =
        source.getPetsMine().mapCatching { result -> result.map { petDto -> petDto.toDomain() } }
}
