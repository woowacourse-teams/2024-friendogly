package com.happy.friendogly.domain

import com.happy.friendogly.domain.error.Error

typealias RootError = Error

sealed interface Result<out D, out E : Error> {
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>

    data class Error<out D, out E : RootError>(val error: E) : Result<D, E>
}

inline fun <D, E : RootError, R> Result<D, E>.fold(
    onSuccess: (data: D) -> R,
    onError: (error: E) -> R,
): R =
    when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Error -> onError(error)
    }
