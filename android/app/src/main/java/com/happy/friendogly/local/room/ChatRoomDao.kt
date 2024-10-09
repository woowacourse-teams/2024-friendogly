package com.happy.friendogly.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.model.ChatRoomEntity

@Dao
interface ChatRoomDao {
    @Query("SELECT * FROM chat_room WHERE id = :id")
    suspend fun getChatRoomById(id: Long): ChatRoomEntity?

    @Update
    suspend fun updateChatRoom(chatRoom: ChatRoomEntity)

    @Insert
    suspend fun insertChatRoom(chatRoom: ChatRoomEntity)

    @Insert
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Insert
    suspend fun insertChatMessages(vararg messages: ChatMessageEntity)

    @Transaction
    suspend fun addNewMessageToChatRoom(
        chatRoomId: Long,
        newMessage: ChatMessageEntity,
    ) {
        getChatRoomById(chatRoomId) ?: insertChatRoom(ChatRoomEntity(chatRoomId))
        insertChatMessage(newMessage)
    }

    @Transaction
    suspend fun addNewMessagesToChatRoom(
        chatRoomId: Long,
        newMessages: List<ChatMessageEntity>,
    ) {
        getChatRoomById(chatRoomId) ?: insertChatRoom(ChatRoomEntity(chatRoomId))
        insertChatMessages(*newMessages.toTypedArray())
    }

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
