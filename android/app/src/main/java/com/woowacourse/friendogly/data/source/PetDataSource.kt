package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.PetDto

interface PetDataSource {
    suspend fun getPetsMine(): Result<List<PetDto>>
}
