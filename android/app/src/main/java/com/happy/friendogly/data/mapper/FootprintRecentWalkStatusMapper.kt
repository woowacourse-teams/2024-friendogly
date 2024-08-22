package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.FootprintRecentWalkStatusDto
import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus

fun FootprintRecentWalkStatusDto.toDomain(): FootprintRecentWalkStatus {
    return FootprintRecentWalkStatus(
        walkStatus = walkStatus.toDomain(),
        changedWalkStatusTime = changedWalkStatusTime,
    )
}
