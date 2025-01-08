package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ChatRoomDto
import com.happy.friendogly.data.model.ChatRoomListDto
import com.happy.friendogly.remote.model.response.ChatRoomListResponse

fun ChatRoomListResponse.toData(): ChatRoomListDto =
    ChatRoomListDto(
        myMemberId = myMemberId,
        chatRooms =
            chatRooms.map {
                ChatRoomDto(
                    chatRoomId = it.chatRoomId,
                    clubName = it.title,
                    memberCount = it.memberCount,
                    clubImageUrl = it.imageUrl,
                    recentMessage = it.recentMessage,
                    recentMessageCreatedAt = it.recentMessageCreatedAt,
                )
            },
    )
