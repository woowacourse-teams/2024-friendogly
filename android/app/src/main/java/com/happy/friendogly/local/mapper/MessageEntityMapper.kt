package com.happy.friendogly.local.mapper

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.local.model.ChatMemberEntity
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.room.MessageTypeEntity

fun ChatComponent.Date.toData(): ChatMessageEntity =
    ChatMessageEntity(
        createdAt = createdAt,
        member = ChatMemberEntity.noChatMember(),
        content = ChatMessageEntity.NOT_CONTENT,
        type = MessageTypeEntity.DATE,
    )

fun ChatComponent.Leave.toData(): ChatMessageEntity =
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
    )

fun ChatComponent.Enter.toData(): ChatMessageEntity =
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
    )

fun Message.Mine.toData(): ChatMessageEntity =
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
    )

fun Message.Other.toData(): ChatMessageEntity =
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
    )

fun ChatMemberEntity.toDomain(): ChatMember =
    ChatMember(
        id = id,
        name = name,
        profileImageUrl = profileUrl,
    )

fun ChatMessageEntity.toDomain(myMemberId: Long): ChatComponent =
    when (this.type) {
        MessageTypeEntity.DATE -> ChatComponent.Date(createdAt = createdAt)
        MessageTypeEntity.CHAT -> {
            if (myMemberId == member.id) {
                Message.Mine(
                    content = content,
                    member = member.toDomain(),
                    createdAt = createdAt,
                )
            } else {
                Message.Other(
                    content = content,
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
