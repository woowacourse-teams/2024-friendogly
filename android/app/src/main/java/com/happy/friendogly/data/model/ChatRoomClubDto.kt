package com.happy.friendogly.data.model

data class ChatRoomClubDto(
    val clubId: Long,
    val allowedGender: List<GenderDto>,
    val allowedSize: List<SizeTypeDto>,
)
