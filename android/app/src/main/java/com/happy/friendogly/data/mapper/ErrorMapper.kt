package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.error.ErrorCodeDto
import com.happy.friendogly.domain.error.DataError

fun ErrorCodeDto?.toDomain(): DataError.Network {
    return when (this) {
        ErrorCodeDto.DEFAULT_ERROR_CODE -> DataError.Network.SERVER_ERROR
        null -> DataError.Network.UNKNOWN
    }
}
