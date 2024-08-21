package com.happy.friendogly.domain.model

data class ChatRoomClub(
    val clubId: Long,
    val myMemberId: Long,
    val allowedGender: List<Gender>,
    val allowedSize: List<SizeType>,
)
