package com.woowacourse.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class FootprintSaveResponse(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
)
