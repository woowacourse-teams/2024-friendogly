package com.happy.friendogly.local.mapper

import androidx.room.TypeConverter
import com.happy.friendogly.local.model.ChatMessageEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChatMessagesConverter {
    @TypeConverter
    fun fromChatMessageEntityList(messages: List<ChatMessageEntity>): String {
        return Json.encodeToString(messages)
    }

    @TypeConverter
    fun toChatMessageEntityList(data: String): List<ChatMessageEntity> {
        return Json.decodeFromString<List<ChatMessageEntity>>(data)
    }
}
