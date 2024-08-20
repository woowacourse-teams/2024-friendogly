package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ChatRoomClubDto
import com.happy.friendogly.domain.model.ChatRoomClub

fun ChatRoomClubDto.toDomain(): ChatRoomClub =
    ChatRoomClub(
        clubId = clubId,
        allowedGender = allowedGender.map { it.toDomain() },
        allowedSize = allowedSize.map { it.toDomain() },
    )
