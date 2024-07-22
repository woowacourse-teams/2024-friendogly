package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootPrintInfoDto
import com.woowacourse.friendogly.domain.model.FootPrintInfo
import com.woowacourse.friendogly.domain.model.PetGender
import com.woowacourse.friendogly.domain.model.PetSizeType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun FootPrintInfoDto.toDomain(): FootPrintInfo {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = LocalDate.parse(petBirthDate, formatter)

    return FootPrintInfo(
        memberName = memberName,
        petName = petName,
        petDescription = petDescription,
        petBirthDate = localDate,
        petSizeType = PetSizeType.from(petSizeType),
        petGender = PetGender.from(petGender),
        footprintImageUrl = footprintImageUrl,
        isMine = isMine,
    )
}
