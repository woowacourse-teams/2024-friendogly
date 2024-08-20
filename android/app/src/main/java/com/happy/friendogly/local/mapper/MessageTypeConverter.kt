package com.happy.friendogly.local.mapper

import androidx.room.TypeConverter
import com.happy.friendogly.local.room.MessageTypeEntity

class MessageTypeConverter {
    @TypeConverter
    fun fromMessageType(messageTypeEntity: MessageTypeEntity): String {
        return messageTypeEntity.name
    }

    @TypeConverter
    fun toMessageType(messageTypeEntity: String): MessageTypeEntity {
        return enumValueOf<MessageTypeEntity>(messageTypeEntity)
    }
}
