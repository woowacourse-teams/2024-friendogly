package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.RecentPetDto

interface RecentPetsDataSource {
    suspend fun getRecentPetById(id: Long): Result<RecentPetDto>

    suspend fun getAllRecentPet(): Result<List<RecentPetDto>>

    suspend fun insertRecentPet(
        imgUrl: String,
        name: String,
        id: Long,
    ): Result<Unit>
}
