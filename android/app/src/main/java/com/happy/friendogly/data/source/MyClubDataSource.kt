package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.ClubDto

interface MyClubDataSource {
    suspend fun getMyOwningClubs(): Result<List<ClubDto>>

    suspend fun getParticipatingClubs(): Result<List<ClubDto>>
}
