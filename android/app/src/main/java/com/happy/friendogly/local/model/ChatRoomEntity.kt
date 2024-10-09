package com.happy.friendogly.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "chat_room")
@Serializable
data class ChatRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)
