package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FootprintInfoResponse(
    val memberId: Long,
    val memberName: String,
    val walkStatus: WalkStatusResponse,
    @Serializable(with = LocalDateTimeSerializer::class)
    val changedWalkStatusTime: LocalDateTime,
    val pets: List<PetDetailResponse>,
    val isMine: Boolean,
)
