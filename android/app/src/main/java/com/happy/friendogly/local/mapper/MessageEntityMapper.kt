package com.happy.friendogly.local.mapper

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.local.model.ChatMemberEntity
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.model.MessageTypeEntity

fun List<ChatComponent>.toData(chatRoomId: Long): List<ChatMessageEntity> =
    this.map { chat ->
        when (chat) {
            is ChatComponent.Date -> chat.toData(chatRoomId)
            is ChatComponent.Enter -> chat.toData(chatRoomId)
            is ChatComponent.Leave -> chat.toData(chatRoomId)
            is Message.Mine -> chat.toData(chatRoomId)
            is Message.Other -> chat.toData(chatRoomId)
        }
    }

fun ChatComponent.Date.toData(chatRoomId: Long): ChatMessageEntity =
    ChatMessageEntity(
        createdAt = createdAt,
        member = ChatMemberEntity.noChatMember(),
        content = ChatMessageEntity.NOT_CONTENT,
        type = MessageTypeEntity.DATE,
        chatRoomId = chatRoomId,
    )

fun ChatComponent.Leave.toData(chatRoomId: Long): ChatMessageEntity =
    ChatMessageEntity(
        createdAt = createdAt,
        member =
            ChatMemberEntity(
                id = member.id,
                name = member.name,
                profileUrl = member.profileImageUrl,
            ),
        content = ChatMessageEntity.NOT_CONTENT,
        type = MessageTypeEntity.LEAVE,
        chatRoomId = chatRoomId,
    )

fun ChatComponent.Enter.toData(chatRoomId: Long): ChatMessageEntity =
    ChatMessageEntity(
        createdAt = createdAt,
        member =
            ChatMemberEntity(
                id = member.id,
                name = member.name,
                profileUrl = member.profileImageUrl,
            ),
        content = ChatMessageEntity.NOT_CONTENT,
        type = MessageTypeEntity.ENTER,
        chatRoomId = chatRoomId,
    )

fun Message.Mine.toData(chatRoomId: Long): ChatMessageEntity =
    ChatMessageEntity(
        createdAt = createdAt,
        member =
            ChatMemberEntity(
                id = member.id,
                name = member.name,
                profileUrl = member.profileImageUrl,
            ),
        content = content,
        type = MessageTypeEntity.CHAT,
        chatRoomId = chatRoomId,
    )

fun Message.Other.toData(chatRoomId: Long): ChatMessageEntity =
    ChatMessageEntity(
        createdAt = createdAt,
        member =
            ChatMemberEntity(
                id = member.id,
                name = member.name,
                profileUrl = member.profileImageUrl,
            ),
        content = content,
        type = MessageTypeEntity.CHAT,
        chatRoomId = chatRoomId,
    )

fun ChatMemberEntity.toDomain(): ChatMember =
    ChatMember(
        id = id,
        name = name,
        profileImageUrl = profileUrl ?: "",
    )

fun ChatMessageEntity.toDomain(myMemberId: Long): ChatComponent =
    when (this.type) {
        MessageTypeEntity.DATE -> ChatComponent.Date(createdAt = createdAt)
        MessageTypeEntity.CHAT -> {
            if (myMemberId == member.id) {
                Message.Mine(
                    content = content!!,
                    member = member.toDomain(),
                    createdAt = createdAt,
                )
            } else {
                Message.Other(
                    content = content!!,
                    member = member.toDomain(),
                    createdAt = createdAt,
                )
            }
        }

        MessageTypeEntity.LEAVE ->
            ChatComponent.Leave(
                member = member.toDomain(),
                createdAt = createdAt,
            )

        MessageTypeEntity.ENTER ->
            ChatComponent.Enter(
                member = member.toDomain(),
                createdAt = createdAt,
            )
    }
