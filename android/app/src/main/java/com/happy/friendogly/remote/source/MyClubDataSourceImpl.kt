package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.data.source.MyClubDataSource
import com.happy.friendogly.remote.api.MyClubService
import com.happy.friendogly.remote.mapper.toData

class MyClubDataSourceImpl(
    private val service: MyClubService,
) : MyClubDataSource {
    override suspend fun getMyOwningClubs(): Result<List<ClubDto>> =
        runCatching {
            service.getMyOwningClubs().data.toData()
        }

    override suspend fun getParticipatingClubs(): Result<List<ClubDto>> =
        runCatching {
            service.getParticipatingClubs().data.toData()
        }
}
