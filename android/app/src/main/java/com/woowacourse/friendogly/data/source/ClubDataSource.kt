package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.LocationDto

interface ClubDataSource {
    suspend fun postClub(): Result<LocationDto>

    suspend fun deleteClub(id: Long): Result<LocationDto>

    suspend fun postClubParticipation(): Result<Unit>

    suspend fun getClubMine(): Result<Unit>
}
