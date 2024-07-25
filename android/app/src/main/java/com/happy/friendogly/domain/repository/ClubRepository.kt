package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.Location

interface ClubRepository {
    suspend fun postClub(): Result<Location>

    suspend fun deleteClub(id: Long): Result<Location>

    suspend fun postClubParticipation(): Result<Unit>

    suspend fun getClubMine(): Result<Unit>
}
