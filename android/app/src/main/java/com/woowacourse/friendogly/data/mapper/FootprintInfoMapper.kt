package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintInfoDto
import com.woowacourse.friendogly.domain.model.FootprintInfo

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
