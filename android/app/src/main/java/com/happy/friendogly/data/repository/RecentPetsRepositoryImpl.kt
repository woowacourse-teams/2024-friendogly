package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.RecentPetsDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.RecentPet
import com.happy.friendogly.domain.repository.RecentPetsRepository
import javax.inject.Inject

class RecentPetsRepositoryImpl
    @Inject
    constructor(
        private val dataSource: RecentPetsDataSource,
    ) : RecentPetsRepository {
        override suspend fun getRecentPetById(id: Long): DomainResult<RecentPet, DataError.Local> =
            dataSource.getRecentPetById(id = id).fold(
                onSuccess = { recentPetDto ->
                    DomainResult.Success(recentPetDto.toDomain())
                },
                onFailure = {
                    DomainResult.Error(DataError.Local.LOCAL_ERROR)
                },
            )

        override suspend fun getAllRecentPet(): DomainResult<List<RecentPet>, DataError.Local> =
            dataSource.getAllRecentPet().fold(
                onSuccess = { recentPetDto ->
                    DomainResult.Success(recentPetDto.map { result -> result.toDomain() })
                },
                onFailure = {
                    DomainResult.Error(DataError.Local.LOCAL_ERROR)
                },
            )

        override suspend fun insertRecentPet(
            imgUrl: String,
            name: String,
            id: Long,
        ): DomainResult<Unit, DataError.Local> {
            return dataSource.insertRecentPet(
                imgUrl = imgUrl,
                name = name,
                id = id,
            ).fold(
                onSuccess = { recentPetDto ->
                    DomainResult.Success(recentPetDto)
                },
                onFailure = {
                    DomainResult.Error(DataError.Local.LOCAL_ERROR)
                },
            )
        }
    }
