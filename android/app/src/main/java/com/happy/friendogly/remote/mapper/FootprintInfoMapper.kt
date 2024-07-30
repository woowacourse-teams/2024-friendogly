package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.remote.model.response.FootprintInfoResponse

fun FootprintInfoResponse.toData(): FootprintInfoDto {
    return FootprintInfoDto(
        memberName = memberName,
        petName = petName,
        petDescription = petDescription,
        petBirthDate = petBirthDate,
        petSizeType = petSizeType.toData(),
        petGender = petGender.toData(),
        footprintImageUrl = footprintImageUrl,
        walkStatus = walkStatus,
        startWalkTime = startWalkTime,
        endWalkTime = endWalkTime,
        createdAt = createdAt,
        isMine = isMine,
    )
}
