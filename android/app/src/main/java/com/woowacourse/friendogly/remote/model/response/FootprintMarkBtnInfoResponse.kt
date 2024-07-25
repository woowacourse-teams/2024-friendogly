package com.woowacourse.friendogly.remote.model.response

import com.woowacourse.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FootprintMarkBtnInfoResponse(
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime?,
)
