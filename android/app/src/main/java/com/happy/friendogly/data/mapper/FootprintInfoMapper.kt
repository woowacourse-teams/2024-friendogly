package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.domain.model.FootprintInfo

fun FootprintInfoDto.toDomain(): FootprintInfo {
    return FootprintInfo(
        memberName = memberName,
        petName = petName,
        petDescription = petDescription,
        petBirthDate = petBirthDate,
        petSizeType = petSizeType.toDomain(),
        petGender = petGender.toDomain(),
        footprintImageUrl = footprintImageUrl,
        createdAt = createdAt,
        isMine = isMine,
    )
}
