package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomResponse(
    val chatRoomId: Long,
    val clubName: String,
    val memberCount: Int,
    val clubImageUrl:String,
)
