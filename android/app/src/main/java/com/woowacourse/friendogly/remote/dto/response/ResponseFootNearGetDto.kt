package com.woowacourse.friendogly.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseFootNearGetDto(
    val memberId: Long,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
)
