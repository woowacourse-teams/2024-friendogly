package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ChatMemberDto
import com.happy.friendogly.data.model.ChatRoomListDto
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.ChatRoom
import com.happy.friendogly.domain.model.ChatRooms

fun ChatMemberDto.toDomain(): ChatMember =
    ChatMember(
        isOwner = isOwner,
        id = memberId,
        name = memberName,
        profileImageUrl = memberProfileImageUrl,
    )

fun ChatRoomListDto.toDomain(): ChatRooms =
    ChatRooms(
        myMemberId = myMemberId,
        chatRooms =
            chatRooms.map {
                ChatRoom(
                    chatRoomId = it.chatRoomId,
                    clubName = it.clubName,
                    memberCount = it.memberCount,
                    clubImageUrl = it.clubImageUrl,
                    recentMessage = it.recentMessage,
                    recentMessageCreatedAt = it.recentMessageCreatedAt,
                )
            },
    )
