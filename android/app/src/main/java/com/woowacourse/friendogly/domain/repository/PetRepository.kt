package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.Pet

interface PetRepository {
    suspend fun getPetsMine(): Result<List<Pet>>
}
