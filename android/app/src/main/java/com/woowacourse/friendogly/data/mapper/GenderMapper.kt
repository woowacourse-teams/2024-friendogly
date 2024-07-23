package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.GenderDto
import com.woowacourse.friendogly.domain.model.Gender

fun GenderDto.toDomain(): Gender {
    return when (this) {
        GenderDto.MALE -> Gender.MALE
        GenderDto.FEMALE -> Gender.FEMALE
        GenderDto.MALE_NEUTERED -> Gender.MALE_NEUTERED
        GenderDto.FEMALE_NEUTERED -> Gender.FEMALE_NEUTERED
    }
}
