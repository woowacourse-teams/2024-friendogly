package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomListResponse(
    val myMemberId:Long,
    val chatRooms:List<ChatRoomResponse>
)
