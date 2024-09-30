package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.RecentPet

interface RecentPetsRepository {
    suspend fun getRecentPetById(id: Long): DomainResult<RecentPet, DataError.Local>

    suspend fun getAllRecentPet(): DomainResult<List<RecentPet>, DataError.Local>

    suspend fun insertRecentPet(
        imgUrl: String,
        name: String,
        id: Long,
    ): DomainResult<Unit, DataError.Local>
}
