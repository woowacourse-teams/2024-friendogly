package com.happy.friendogly.domain

import com.happy.friendogly.domain.error.Error

typealias RootError = Error

sealed interface DomainResult<out D, out E : Error> {
    data class Success<out D, out E : RootError>(val data: D) : DomainResult<D, E>

    data class Error<out D, out E : RootError>(val error: E) : DomainResult<D, E>
}

inline fun <D, E : RootError, R> DomainResult<D, E>.fold(
    onSuccess: (data: D) -> R,
    onError: (error: E) -> R,
): R =
    when (this) {
        is DomainResult.Success -> onSuccess(data)
        is DomainResult.Error -> onError(error)
    }
