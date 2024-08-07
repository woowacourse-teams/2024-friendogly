package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ChatRoomDto
import com.happy.friendogly.data.model.ChatRoomListDto
import com.happy.friendogly.remote.model.response.ChatRoomListResponse

fun ChatRoomListResponse.toData(): ChatRoomListDto = ChatRoomListDto(
    myMemberId = myMemberId,
    chatRooms = chatRooms.map { ChatRoomDto(
        chatRoomId = it.chatRoomId,
        clubName = it.clubName,
        memberCount = it.memberCount,
        clubImageUrl = it.clubImageUrl
    ) }
)
