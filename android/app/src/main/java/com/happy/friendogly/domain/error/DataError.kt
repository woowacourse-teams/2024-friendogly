package com.happy.friendogly.domain.error

sealed interface DataError : Error {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        FILE_SIZE_EXCEED,
        SERIALIZATION,
        UNKNOWN,
    }

    enum class Local : DataError {
        LOCAL_ERROR,
        TOKEN_NOT_STORED,
    }
}
