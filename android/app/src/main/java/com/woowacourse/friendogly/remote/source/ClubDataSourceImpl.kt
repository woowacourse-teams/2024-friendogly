package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.LocationDto
import com.woowacourse.friendogly.data.source.ClubDataSource
import com.woowacourse.friendogly.remote.api.ClubService
import com.woowacourse.friendogly.remote.mapper.toData

class ClubDataSourceImpl(private val service: ClubService) : ClubDataSource {
    override suspend fun postClub(): Result<LocationDto> =
        runCatching {
            service.postClub().data.toData()
        }

    override suspend fun deleteClub(id: Long): Result<LocationDto> =
        runCatching {
            service.deleteClub(id = id).data.toData()
        }

    override suspend fun postClubParticipation(): Result<Unit> =
        runCatching {
            service.postClubParticipation().data
        }

    override suspend fun getClubMine(): Result<Unit> =
        runCatching {
            service.getClubMine().data
        }
}
