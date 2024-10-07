package com.happy.friendogly.data.error

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import java.net.ConnectException
import java.net.UnknownHostException

fun <T> Throwable.toDomainError(): DomainResult.Error<T, DataError.Network> {
    return when (this) {
        is ApiExceptionDto -> DomainResult.Error(this.error.data.errorCode.toDomain())
        is ConnectException, is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
        else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
    }
}
