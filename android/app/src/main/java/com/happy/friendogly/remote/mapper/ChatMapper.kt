package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ChatMemberDto
import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.data.model.MessageTypeDto
import com.happy.friendogly.remote.model.response.ChatClubMemberResponse
import com.happy.friendogly.remote.model.response.ChatMessageResponse
import com.happy.friendogly.remote.model.response.MessageTypeResponse


fun MessageTypeResponse.toData(): MessageTypeDto = when (this) {
    MessageTypeResponse.ENTER -> MessageTypeDto.ENTER
    MessageTypeResponse.CHAT -> MessageTypeDto.CHAT
    MessageTypeResponse.LEAVE -> MessageTypeDto.LEAVE
}

fun ChatMessageResponse.toData(): MessageDto = MessageDto(
    messageType = messageType.toData(),
    senderMemberId = senderMemberId,
    senderName = senderName,
    content = content,
    createdAt = createdAt,
    profilePictureUrl = profilePictureUrl
)

fun ChatClubMemberResponse.toData(): ChatMemberDto = ChatMemberDto(
    isOwner = isOwner,
    memberId = memberId,
    memberName = memberName,
    memberProfileImageUrl = memberProfileImageUrl
)
