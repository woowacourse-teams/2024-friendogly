package com.happy.friendogly.domain

import com.happy.friendogly.domain.error.Error

typealias RootError = Error

sealed interface Result<out D, out E : Error> {
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>

    data class Error<out D, out E : RootError>(val error: E) : Result<D, E>
}

