package com.woowacourse.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class FootprintRequest(
    val latitude: Double,
    val longitude: Double,
)
