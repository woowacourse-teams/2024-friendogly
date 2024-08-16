package com.happy.friendogly.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatMember")
data class ChatMemberEntity(
    @PrimaryKey val id: Long = 0,
)
