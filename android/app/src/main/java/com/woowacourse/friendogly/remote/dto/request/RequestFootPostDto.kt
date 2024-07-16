package com.woowacourse.friendogly.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestFootPostDto(
    val memberId: Long,
    val latitude: Double,
    val longitude: Double,
)
