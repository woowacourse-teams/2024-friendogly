package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.MyClubDataSource
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.repository.MyClubRepository
import javax.inject.Inject

class MyClubRepositoryImpl
    @Inject
    constructor(
        private val source: MyClubDataSource,
    ) : MyClubRepository {
        override suspend fun getMyClubs(): Result<List<Club>> = source.getParticipatingClubs().mapCatching { it.toDomain() }

        override suspend fun getMyHeadClubs(): Result<List<Club>> = source.getMyOwningClubs().mapCatching { it.toDomain() }
    }
