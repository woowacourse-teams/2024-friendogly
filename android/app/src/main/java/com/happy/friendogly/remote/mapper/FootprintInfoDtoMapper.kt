package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.remote.model.response.FootprintInfoResponse

fun FootprintInfoResponse.toData(): FootprintInfoDto {
    return FootprintInfoDto(
        memberName = memberName,
        walkStatus = walkStatus.toData(),
        changedWalkStatusTime = changedWalkStatusTime,
        pets = pets.toData(),
        isMine = isMine,
    )
}
