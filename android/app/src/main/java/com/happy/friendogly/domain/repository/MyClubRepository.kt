package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Club

interface MyClubRepository {
    suspend fun getMyClubs(): DomainResult<List<Club>,DataError.Network>

    suspend fun getMyHeadClubs(): DomainResult<List<Club>,DataError.Network>
}
