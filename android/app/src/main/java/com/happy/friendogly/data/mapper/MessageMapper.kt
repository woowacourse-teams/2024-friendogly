package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.data.model.MessageTypeDto
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.Message

fun List<MessageDto>.toDomain(myMemberId:Long) = this.map { message ->
    when(message.messageType) {
        MessageTypeDto.ENTER -> message.toEnter()
        MessageTypeDto.LEAVE -> message.toLeave()
        MessageTypeDto.CHAT -> if (myMemberId == message.senderMemberId) {
            message.toMine()
        } else {
            message.toOther()
        }
    }

}

fun MessageDto.toOther(): Message.Other =
    Message.Other(
        member =
            ChatMember(
                id = senderMemberId,
                name = senderName,
                profileImageUrl = profilePictureUrl ?: "",
            ),
        content = content ?: "",
        createdAt = createdAt,
    )

fun MessageDto.toMine(): Message.Mine =
    Message.Mine(
        member =
            ChatMember(
                id = senderMemberId,
                name = senderName,
                profileImageUrl = profilePictureUrl ?: "",
            ),
        content = content ?: "",
        createdAt = createdAt,
    )

fun MessageDto.toEnter(): ChatComponent.Enter =
    ChatComponent.Enter(
        member =
            ChatMember(
                id = senderMemberId,
                name = senderName,
                profileImageUrl = profilePictureUrl ?: "",
            ),
        createdAt = createdAt,
    )

fun MessageDto.toLeave(): ChatComponent.Leave =
    ChatComponent.Leave(
        member =
            ChatMember(
                id = senderMemberId,
                name = senderName,
                profileImageUrl = profilePictureUrl ?: "",
            ),
        createdAt = createdAt,
    )
