package com.happy.friendogly

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.happy.friendogly.local.dao.ChatMessageDao
import com.happy.friendogly.local.model.ChatMemberEntity
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.model.MessageTypeEntity
import com.happy.friendogly.local.room.ChatMessageDatabase
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class ChatMessageDatabaseTest {
    private lateinit var chatMessageDao: ChatMessageDao

    private lateinit var db: ChatMessageDatabase

    @Before
    fun setUp() {
        db =
            Room
                .inMemoryDatabaseBuilder(
                    context = ApplicationProvider.getApplicationContext<Context>(),
                    klass = ChatMessageDatabase::class.java,
                ).build()

        chatMessageDao = db.chatMessageDao()
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
    fun `can_get_latest_mesage`() {
        // when
        runBlocking {
            chatMessageDao.insert(DUMMY_MY_MESSAGE)
            chatMessageDao.insert(DUMMY_CHAT_MESSAGE)
            chatMessageDao.insert(DUMMY_CHAT_MESSAGE2)
        }

        // then
        runBlocking {
            val message = chatMessageDao.getLatestMessageByRoomId(2)
            assertThat(message).isEqualTo(DUMMY_CHAT_MESSAGE2)
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
                chatRoomId = 2,
            )

        private val DUMMY_CHAT_MESSAGE =
            ChatMessageEntity(
                createdAt = LocalDateTime.of(2024, 6, 20, 14, 2),
                member =
                    ChatMemberEntity(
                        2,
                        "벼리",
                        "",
                    ),
                content = "ZZZZZZZZ",
                type = MessageTypeEntity.CHAT,
                id = 2,
                chatRoomId = 2,
            )

        private val DUMMY_CHAT_MESSAGE2 =
            ChatMessageEntity(
                createdAt = LocalDateTime.of(2024, 7, 12, 14, 2),
                member =
                    ChatMemberEntity(
                        2,
                        "벼리",
                        "",
                    ),
                content = "ZZZZZZZZ",
                type = MessageTypeEntity.CHAT,
                id = 3,
                chatRoomId = 2,
            )
    }
}
