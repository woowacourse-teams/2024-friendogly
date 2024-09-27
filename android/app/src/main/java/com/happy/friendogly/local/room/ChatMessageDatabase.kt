package com.happy.friendogly.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.happy.friendogly.local.dao.ChatMessageDao
import com.happy.friendogly.local.dao.ChatRoomDao
import com.happy.friendogly.local.mapper.ChatMessagesConverter
import com.happy.friendogly.local.mapper.LocalDateTimeConverter
import com.happy.friendogly.local.mapper.MessageTypeConverter
import com.happy.friendogly.local.model.ChatMemberEntity
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.model.ChatRoomEntity

@Database(
    entities = [ChatMessageEntity::class, ChatMemberEntity::class, ChatRoomEntity::class],
    version = 1,
)
@TypeConverters(value = [LocalDateTimeConverter::class, ChatMessagesConverter::class, MessageTypeConverter::class])
abstract class ChatMessageDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao

    abstract fun chatRoomDao(): ChatRoomDao
}
