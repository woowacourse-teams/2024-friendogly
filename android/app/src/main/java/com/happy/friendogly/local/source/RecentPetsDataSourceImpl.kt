package com.happy.friendogly.local.source

import com.happy.friendogly.data.mapper.toLocal
import com.happy.friendogly.data.model.RecentPetDto
import com.happy.friendogly.data.source.RecentPetsDataSource
import com.happy.friendogly.local.dao.RecentPetsDao
import com.happy.friendogly.local.mapper.toData
import javax.inject.Inject

class RecentPetsDataSourceImpl
    @Inject
    constructor(
        private val dao: RecentPetsDao,
    ) : RecentPetsDataSource {
        override suspend fun getRecentPetById(id: Long): Result<RecentPetDto> =
            runCatching {
                dao.getRecentPetById(id = id)?.toData()
                    ?: throw IllegalArgumentException("존재하지 않는 최근 본 강아지 id 입니다.")
            }

        override suspend fun getAllRecentPet(): Result<List<RecentPetDto>> =
            runCatching {
                dao.getAllRecentPet().map { result -> result.toData() }
            }

        override suspend fun insertRecentPet(
            imgUrl: String,
            name: String,
            id: Long,
        ): Result<Unit> =
            runCatching {
                val recentPetDto =
                    RecentPetDto(
                        memberId = id,
                        imgUrl = imgUrl,
                        name = name,
                    )
                dao.insertRecentPet(recentPetEntity = recentPetDto.toLocal())
            }
    }
