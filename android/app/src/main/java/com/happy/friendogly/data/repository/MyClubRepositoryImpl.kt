package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.toDomainError
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.MyClubDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.repository.MyClubRepository
import javax.inject.Inject

class MyClubRepositoryImpl
    @Inject
    constructor(
        private val source: MyClubDataSource,
    ) : MyClubRepository {
        override suspend fun getMyClubs(): DomainResult<List<Club>, DataError.Network> {
            return source.getParticipatingClubs().fold(
                onSuccess = { clubs ->
                    DomainResult.Success(clubs.toDomain())
                },
                onFailure = { error ->
                    error.toDomainError()
                },
            )
        }

        override suspend fun getMyHeadClubs(): DomainResult<List<Club>, DataError.Network> {
            return source.getMyOwningClubs().fold(
                onSuccess = { clubs ->
                    DomainResult.Success(clubs.toDomain())
                },
                onFailure = { error ->
                    error.toDomainError()
                },
            )
        }
    }
