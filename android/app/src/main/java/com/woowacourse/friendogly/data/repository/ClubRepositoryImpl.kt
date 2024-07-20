package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.ClubDataSource
import com.woowacourse.friendogly.domain.model.Location
import com.woowacourse.friendogly.domain.repository.ClubRepository

class ClubRepositoryImpl(
    private val source: ClubDataSource,
) : ClubRepository {
    override suspend fun postClub(): Result<Location> = source.postClub().mapCatching { result -> result.toDomain() }

    override suspend fun deleteClub(id: Long): Result<Location> = source.deleteClub(id = id).mapCatching { result -> result.toDomain() }

    override suspend fun postClubParticipation(): Result<Unit> = source.postClubParticipation()

    override suspend fun getClubMine(): Result<Unit> = source.getClubMine()
}
