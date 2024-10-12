package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PlaygroundJoinResponse(
    val playgroundId: Long,
    val memberId: Long,
    val message: String,
    val isArrived: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class)
    val exitTime: LocalDateTime?,
)
