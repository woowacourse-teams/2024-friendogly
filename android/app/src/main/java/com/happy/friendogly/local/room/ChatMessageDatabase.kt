package com.happy.friendogly.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.happy.friendogly.local.mapper.LocalDateTimeConverter
import com.happy.friendogly.local.mapper.MessageTypeConverter
import com.happy.friendogly.local.model.ChatMemberEntity
import com.happy.friendogly.local.model.ChatMessageEntity

@Database(
    entities = [ChatMessageEntity::class, ChatMemberEntity::class],
    version = 1,
)
@TypeConverters(value = [LocalDateTimeConverter::class,MessageTypeConverter::class])
abstract class ChatMessageDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}
