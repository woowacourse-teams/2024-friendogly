package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ChatRoomClubDto
import com.happy.friendogly.remote.model.response.ChatRoomClubResponse

fun ChatRoomClubResponse.toData(): ChatRoomClubDto =
    ChatRoomClubDto(
        clubId = clubId,
        allowedGender = allowedGenders.map { it.toData() },
        allowedSize = allowedSizeTypes.map { it.toData() },
    )
