package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintInfoDto
import com.woowacourse.friendogly.domain.model.FootprintInfo
import com.woowacourse.friendogly.domain.model.PetGender
import com.woowacourse.friendogly.domain.model.PetSizeType
import com.woowacourse.friendogly.presentation.utils.parseToLocalDate
import com.woowacourse.friendogly.presentation.utils.parseToLocalDateTimeOrNull
import com.woowacourse.friendogly.remote.model.response.FootprintInfoResponse

fun FootprintInfoDto.toDomain(): FootprintInfo {
    return FootprintInfo(
        memberName = memberName,
        petName = petName,
        petDescription = petDescription,
        petBirthDate = parseToLocalDate(petBirthDate),
        petSizeType = PetSizeType.from(petSizeType),
        petGender = PetGender.from(petGender),
        footprintImageUrl = footprintImageUrl,
        createdAt = parseToLocalDateTimeOrNull(createdAt),
        isMine = isMine,
    )
}

fun FootprintInfoResponse.toData(): FootprintInfoDto {
    return FootprintInfoDto(
        memberName = memberName,
        petName = petName,
        petDescription = petDescription,
        petBirthDate = petBirthDate,
        petSizeType = petSizeType,
        petGender = petGender,
        footprintImageUrl = footprintImageUrl,
        createdAt = createdAt,
        isMine = isMine,
    )
}
