package com.happy.friendogly.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "chat_room")
@Serializable
data class ChatRoomEntity(
    @ColumnInfo(name = "chat_messages") val messages: List<ChatMessageEntity>,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)
