package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.FootprintRecentWalkStatusDto
import com.happy.friendogly.remote.model.response.FootprintWalkStatusResponse

fun FootprintWalkStatusResponse.toData(): FootprintRecentWalkStatusDto {
    return FootprintRecentWalkStatusDto(
        walkStatus = walkStatus.toData(),
        changedWalkStatusTime = changedWalkStatusTime,
    )
}
