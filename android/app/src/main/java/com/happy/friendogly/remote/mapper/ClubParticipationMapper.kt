package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ClubParticipationDto
import com.happy.friendogly.remote.model.response.ClubParticipationResponse

fun ClubParticipationResponse.toData(): ClubParticipationDto {
    return ClubParticipationDto(
        memberId = memberId,
        chatRoomId = chatRoomId,
    )
}
