package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.error.ErrorCodeDto
import com.happy.friendogly.domain.error.DataError

fun ErrorCodeDto?.toDomain(): DataError.Network {
    return when (this) {
        ErrorCodeDto.DEFAULT_ERROR_CODE -> DataError.Network.SERVER_ERROR
        ErrorCodeDto.FILE_SIZE_EXCEED -> DataError.Network.FILE_SIZE_EXCEED
        ErrorCodeDto.OVERLAP_PLAYGROUND_CREATION -> DataError.Network.OVERLAP_PLAYGROUND_CREATION
        ErrorCodeDto.ALREADY_PARTICIPATE_PLAYGROUND -> DataError.Network.ALREADY_PARTICIPATE_PLAYGROUND
        ErrorCodeDto.NO_PARTICIPATING_PLAYGROUND -> DataError.Network.NO_PARTICIPATING_PLAYGROUND
        ErrorCodeDto.CLUB_SIZE_EXCEED -> TODO()
        null -> DataError.Network.UNKNOWN
    }
}
