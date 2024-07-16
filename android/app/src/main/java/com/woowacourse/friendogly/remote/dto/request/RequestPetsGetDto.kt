package com.woowacourse.friendogly.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestPetsGetDto(
    val memberId: Long
)
