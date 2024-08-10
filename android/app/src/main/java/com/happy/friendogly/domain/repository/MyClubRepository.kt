package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.Club

interface MyClubRepository {
    suspend fun getMyClubs(): Result<List<Club>>

    suspend fun getMyHeadClubs(): Result<List<Club>>
}
