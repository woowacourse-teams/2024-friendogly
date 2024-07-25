package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.FootprintMarkBtnInfoDto
import com.happy.friendogly.domain.model.FootprintMarkBtnInfo

fun FootprintMarkBtnInfoDto.toDomain(): FootprintMarkBtnInfo {
    return FootprintMarkBtnInfo(createdAt = createdAt, hasPet = hasPet)
}
