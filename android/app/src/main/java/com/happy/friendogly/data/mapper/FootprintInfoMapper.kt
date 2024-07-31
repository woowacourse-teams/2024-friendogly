package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo

fun FootprintInfoDto.toDomain(): FootprintInfo {
    return FootprintInfo(
        memberName = memberName,
        walkStatus = walkStatus.toDomain(),
        changedWalkStatusTime = changedWalkStatusTime,
        pets = pets.toDomain(),
        isMine = isMine,
    )
}
