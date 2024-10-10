package com.happy.friendogly.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.happy.friendogly.local.model.ChatMessageEntity

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chatMessageEntity: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg chatMessageEntity: ChatMessageEntity)

    @Query("SELECT * FROM chat_message")
    suspend fun getAll(): List<ChatMessageEntity>

    @Query("SELECT * FROM chat_message WHERE id == :chatRoomId")
    suspend fun getMessagesByRoomId(chatRoomId: Long): List<ChatMessageEntity>

    @Query("SELECT * FROM chat_message WHERE chat_room_id == :chatRoomId ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestMessageByRoomId(chatRoomId: Long): ChatMessageEntity?

    @Query("SELECT * FROM chat_message ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getMessagesInRange(
        limit: Int,
        offset: Int,
    ): List<ChatMessageEntity>

}
