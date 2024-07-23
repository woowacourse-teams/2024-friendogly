package com.woowacourse.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
enum class GenderResponse {
    MALE,
    FEMALE,
    MALE_NEUTERED,
    FEMALE_NEUTERED,
}
