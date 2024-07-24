package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.FootprintInfoDto
import com.woowacourse.friendogly.remote.model.response.FootprintInfoResponse

fun FootprintInfoResponse.toData(): FootprintInfoDto {
    return FootprintInfoDto(
        memberName = memberName,
        petName = petName,
        petDescription = petDescription,
        petBirthDate = petBirthDate,
        petSizeType = petSizeType.toData(),
        petGender = petGender.toData(),
        footprintImageUrl = footprintImageUrl,
        createdAt = createdAt,
        isMine = isMine,
    )
}
