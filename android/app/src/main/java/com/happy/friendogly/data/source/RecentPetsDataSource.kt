package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.data.model.RecentPetDto
import com.happy.friendogly.data.model.SizeTypeDto
import kotlinx.datetime.LocalDate

interface RecentPetsDataSource {
    suspend fun getRecentPetById(id: Long): Result<RecentPetDto>

    suspend fun getAllRecentPet(): Result<List<RecentPetDto>>

    suspend fun insertRecentPet(
        id: Long,
        name: String,
        imgUrl: String,
        birthday: LocalDate,
        gender: GenderDto,
        sizeType: SizeTypeDto,
    ): Result<Unit>
}
