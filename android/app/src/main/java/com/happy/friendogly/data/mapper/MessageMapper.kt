package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.Message

fun MessageDto.toOther(): Message.Other =
    Message.Other(
        memberId = senderMemberId,
        name = senderName,
        content = content ?: "",
        dateTime = createdAt,
        profileUrl = profilePictureUrl,
    )

fun MessageDto.toMine(): Message.Mine =
    Message.Mine(
        content = content ?: "",
        dateTime = createdAt,
    )

fun MessageDto.toEnter(): ChatComponent.Enter =
    ChatComponent.Enter(
        name = senderName,
    )

fun MessageDto.toLeave(): ChatComponent.Leave =
    ChatComponent.Leave(
        name = senderName,
    )
