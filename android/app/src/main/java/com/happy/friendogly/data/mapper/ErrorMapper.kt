package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.error.ErrorCodeDto
import com.happy.friendogly.domain.error.DataError

fun ErrorCodeDto?.toDomain(): DataError.Network {
    return when (this) {
        ErrorCodeDto.DEFAULT_ERROR_CODE -> DataError.Network.SERVER_ERROR
        ErrorCodeDto.FILE_SIZE_EXCEED -> DataError.Network.FILE_SIZE_EXCEED
        null -> DataError.Network.UNKNOWN
    }
}
