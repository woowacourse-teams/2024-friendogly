package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ClubParticipationDto
import com.happy.friendogly.domain.model.ClubParticipation

fun ClubParticipationDto.toDomain(): ClubParticipation{
    return ClubParticipation(
        memberId = memberId,
        chatRoomId = chatRoomId,
    )
}
