package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.data.model.MessageTypeDto
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.local.mapper.toData
import com.happy.friendogly.local.model.ChatMemberEntity
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.room.MessageTypeEntity

fun List<MessageDto>.toDomain(myMemberId: Long): List<ChatComponent> =
    this.map { message ->
        when (message.messageType) {
            MessageTypeDto.ENTER -> message.toEnter()
            MessageTypeDto.LEAVE -> message.toLeave()
            MessageTypeDto.CHAT ->
                classifyChat(myMemberId, message)
        }
    }

private fun classifyChat(
    myMemberId: Long,
    message: MessageDto,
) = if (myMemberId == message.senderMemberId) {
    message.toMine()
} else {
    message.toOther()
}

fun ChatComponent.toData(chatRoomId: Long): ChatMessageEntity =
    when (this) {
        is ChatComponent.Date -> this.toData(chatRoomId)
        is ChatComponent.Enter -> this.toData(chatRoomId)
        is ChatComponent.Leave -> this.toData(chatRoomId)
        is Message.Mine -> this.toData(chatRoomId)
        is Message.Other -> this.toData(chatRoomId)
    }

fun List<MessageDto>.toLocalData(chatRoomId: Long): List<ChatMessageEntity> =
    this.map {
        ChatMessageEntity(
            createdAt = it.createdAt,
            member =
                ChatMemberEntity(
                    id = it.senderMemberId,
                    name = it.senderName,
                    profileUrl = it.profilePictureUrl,
                ),
            content = it.content,
            type = it.messageType.toLocalData(),
            chatRoomId = chatRoomId,
        )
    }

private fun MessageTypeDto.toLocalData() =
    when (this) {
        MessageTypeDto.ENTER -> MessageTypeEntity.ENTER
        MessageTypeDto.CHAT -> MessageTypeEntity.CHAT
        MessageTypeDto.LEAVE -> MessageTypeEntity.LEAVE
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
