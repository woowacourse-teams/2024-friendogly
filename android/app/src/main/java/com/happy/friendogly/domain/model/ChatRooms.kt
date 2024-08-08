package com.happy.friendogly.domain.model

data class ChatRooms(
    val myMemberId: Long,
    val chatRooms: List<ChatRoom>,
)
