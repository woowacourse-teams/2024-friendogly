package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintInfoDto
import com.woowacourse.friendogly.domain.model.FootprintInfo
import com.woowacourse.friendogly.remote.mapper.toData
import com.woowacourse.friendogly.remote.model.response.FootprintInfoResponse

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
