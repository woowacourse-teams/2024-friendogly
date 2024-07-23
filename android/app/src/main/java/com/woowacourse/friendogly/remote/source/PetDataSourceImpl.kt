package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.PetDto
import com.woowacourse.friendogly.data.source.PetDataSource
import com.woowacourse.friendogly.remote.api.PetService
import com.woowacourse.friendogly.remote.mapper.toData

class PetDataSourceImpl(private val service: PetService) : PetDataSource {
    override suspend fun getPetsMine(): Result<List<PetDto>> =
        runCatching {
            service.getPetsMine().map { petResponse -> petResponse.toData() }
        }
}
