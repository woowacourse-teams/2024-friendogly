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
    suspend fun insert(chatRoom: ChatRoomEntity)

    @Transaction
    suspend fun addMessageToChatRoom(
        chatRoomId: Long,
        newMessage: ChatMessageEntity,
    ) {
        val chatRoom = getChatRoomById(chatRoomId)
        if (chatRoom != null) {
            val updatedMessages =
                chatRoom.messages.toMutableList().apply {
                    add(newMessage)
                }
            val updatedChatRoom = chatRoom.copy(messages = updatedMessages)
            updateChatRoom(updatedChatRoom)
        } else {
            insert(ChatRoomEntity(id = chatRoomId, messages = listOf(newMessage)))
        }
    }

    @Transaction
    suspend fun addMessagesToChatRoom(
        chatRoomId: Long,
        newMessages: List<ChatMessageEntity>,
    ) {
        val chatRoom = getChatRoomById(chatRoomId)
        if (chatRoom != null) {
            val updatedMessages = chatRoom.messages.toMutableList()

            newMessages.forEach { newMessage ->
                updatedMessages.find {
                    it.createdAt == newMessage.createdAt && it.content == newMessage.content
                } ?: return@forEach

                updatedMessages.add(newMessage)
            }

            val updatedChatRoom = chatRoom.copy(messages = updatedMessages)
            updateChatRoom(updatedChatRoom)
        } else {
            insert(ChatRoomEntity(id = chatRoomId, messages = newMessages))
        }
    }


    @Transaction
    suspend fun getMessagesByRoomId(chatRoomId: Long): List<ChatMessageEntity> {
        val chatRoom = getChatRoomById(chatRoomId)
        return chatRoom?.messages ?: emptyList()
    }
}
