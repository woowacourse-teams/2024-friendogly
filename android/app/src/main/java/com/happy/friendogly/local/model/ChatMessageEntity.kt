package com.happy.friendogly.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Contextual
import java.time.LocalDateTime

@Entity(tableName = "chat_message")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: @Contextual LocalDateTime,
    @Embedded(prefix = "member_")
    val member: ChatMemberEntity,
    @ColumnInfo(name = "content")
    val content: String,
)
