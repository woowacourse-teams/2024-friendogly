package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.FootprintMarkBtnInfoDto
import com.happy.friendogly.remote.model.response.FootprintMarkBtnInfoResponse

fun FootprintMarkBtnInfoResponse.toData(): FootprintMarkBtnInfoDto {
    return FootprintMarkBtnInfoDto(
        createdAt = createdAt,
        hasPet = hasPet,
    )
}
