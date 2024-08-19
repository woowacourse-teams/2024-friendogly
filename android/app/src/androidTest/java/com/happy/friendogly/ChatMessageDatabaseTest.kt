package com.happy.friendogly

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.happy.friendogly.local.model.ChatMemberEntity
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.room.ChatMessageDao
import com.happy.friendogly.local.room.ChatMessageDatabase
import com.happy.friendogly.local.room.ChatRoomDao
import com.happy.friendogly.local.room.MessageTypeEntity
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class ChatMessageDatabaseTest {
    private lateinit var chatMessageDao: ChatMessageDao
    private lateinit var chatRoomDao: ChatRoomDao

    private lateinit var db: ChatMessageDatabase

    @Before
    fun setUp() {
        db =
            Room.inMemoryDatabaseBuilder(
                context = ApplicationProvider.getApplicationContext<Context>(),
                klass = ChatMessageDatabase::class.java,
            ).build()

        chatMessageDao = db.chatMessageDao()
        chatRoomDao = db.chatRoomDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `can_save_chat_message_and_get_the_data`() {
        // when
        runBlocking {
            chatMessageDao.insert(
                DUMMY_MY_MESSAGE,
            )
        }

        // then
        runBlocking {
            val actual: List<ChatMessageEntity> = chatMessageDao.getAll()
            assertThat(actual).contains(DUMMY_MY_MESSAGE)
        }
    }

    @Test
    fun `can_update_chatRoom_message`() {
        // when
        runBlocking {
            chatRoomDao.addMessageToChatRoom(2, DUMMY_MY_MESSAGE)
            chatRoomDao.addMessageToChatRoom(2, DUMMY_CHAT_MESSAGE)
        }

        runBlocking {
            val messages = chatRoomDao.getMessagesByRoomId(2)
            assertThat(messages).contains(DUMMY_MY_MESSAGE, DUMMY_CHAT_MESSAGE)
        }
    }

    companion object {
        private val DUMMY_MY_MESSAGE =
            ChatMessageEntity(
                createdAt = LocalDateTime.of(2024, 6, 12, 14, 2),
                member =
                    ChatMemberEntity(
                        1,
                        "벼리",
                        "",
                    ),
                content = "",
                type = MessageTypeEntity.CHAT,
                id = 1,
            )

        private val DUMMY_CHAT_MESSAGE =
            ChatMessageEntity(
                createdAt = LocalDateTime.of(2024, 6, 12, 14, 2),
                member =
                    ChatMemberEntity(
                        2,
                        "벼리",
                        "",
                    ),
                content = "ZZZZZZZZ",
                type = MessageTypeEntity.CHAT,
                id = 2,
            )
    }
}
