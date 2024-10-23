package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.RecentPet
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.LocalDate

interface RecentPetsRepository {
    suspend fun getRecentPetById(id: Long): DomainResult<RecentPet, DataError.Local>

    suspend fun getAllRecentPet(): DomainResult<List<RecentPet>, DataError.Local>

    suspend fun insertRecentPet(
        memberId: Long,
        petId: Long,
        name: String,
        imgUrl: String,
        birthday: LocalDate,
        gender: Gender,
        sizeType: SizeType,
    ): DomainResult<Unit, DataError.Local>
}
