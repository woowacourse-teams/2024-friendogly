package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubParticipationResponse(
    val memberId: Long,
    val chatRoomId: Long,
)
