package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.LocationDto
import com.happy.friendogly.data.source.ClubDataSource
import com.happy.friendogly.remote.api.ClubService
import com.happy.friendogly.remote.mapper.toData

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
