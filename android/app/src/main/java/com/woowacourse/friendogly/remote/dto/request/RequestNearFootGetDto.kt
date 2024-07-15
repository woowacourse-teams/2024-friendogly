package com.woowacourse.friendogly.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestNearFootGetDto(
    val latitude: Double,
    val longitude: Double
)
