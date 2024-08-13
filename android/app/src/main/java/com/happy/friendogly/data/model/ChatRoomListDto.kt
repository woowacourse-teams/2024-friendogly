package com.happy.friendogly.data.model

data class ChatRoomListDto(
    val myMemberId: Long,
    val chatRooms: List<ChatRoomDto>,
)
