package com.happy.friendogly.data.repository

import com.happy.friendogly.data.source.MessagingDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.MessagingRepository

class MessagingRepositoryImpl(
    private val source: MessagingDataSource,
) : MessagingRepository {
    override suspend fun getToken(): DomainResult<String, DataError.Local> {
        return source.getToken().fold(
            onSuccess = { token ->
                DomainResult.Success(token)
            },
            onFailure = { e ->
                DomainResult.Error(DataError.Local.LOCAL_ERROR)
            },
        )
    }
}
