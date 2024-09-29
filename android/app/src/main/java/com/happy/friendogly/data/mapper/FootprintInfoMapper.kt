package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo

fun FootprintInfoDto.toDomain(): PlaygroundInfo {
    return PlaygroundInfo(
        memberId = memberId,
        memberName = memberName,
        walkStatus = walkStatus.toDomain(),
        changedWalkStatusTime = changedWalkStatusTime,
        petDetails = pets.toDomain(),
        isMine = isMine,
    )
}
