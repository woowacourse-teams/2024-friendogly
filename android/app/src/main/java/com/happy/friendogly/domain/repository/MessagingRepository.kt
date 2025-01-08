package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError

interface MessagingRepository {
    suspend fun getToken(): DomainResult<String, DataError.Local>
}
