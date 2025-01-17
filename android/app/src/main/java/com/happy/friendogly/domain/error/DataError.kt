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
        OVERLAP_PLAYGROUND_CREATION,
        ALREADY_PARTICIPATE_PLAYGROUND,
        NO_PARTICIPATING_PLAYGROUND,
        UNKNOWN,
        CLUB_PARTICIPATION_EXCEED,
        CLUB_OPEN_EXCEED,
    }

    enum class Local : DataError {
        LOCAL_ERROR,
        TOKEN_NOT_STORED,
    }
}
