package com.happy.friendogly

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Assertions.assertThat

import com.happy.friendogly.local.model.ChatMemberEntity
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.room.ChatMessageDao
import com.happy.friendogly.local.room.ChatMessageDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class ChatMessageDatabaseTest {
    private lateinit var chatMessageDao: ChatMessageDao

    private lateinit var db: ChatMessageDatabase

    @Before
    fun setUp()  {
        db =
            Room.inMemoryDatabaseBuilder(
                context = ApplicationProvider.getApplicationContext<Context>(),
                klass = ChatMessageDatabase::class.java,
            ).build()

        chatMessageDao = db.chatMessageDao()
    }

    @After
    fun closeDb()  {
        db.close()
    }

    @Test
    fun `can_save_chat_message_and_get_the_data`()  {
        // given
        val message  =ChatMessageEntity(
            1,
            createdAt = LocalDateTime.now(),
            member =
            ChatMemberEntity(
                1,
                "벼리",
                "",
            ),
            content = "",
        )

        // when
        chatMessageDao.insert(
            message
        )

        // then
        val actual :List<ChatMessageEntity> = chatMessageDao.getAll()
        assertThat(actual).contains(message)
    }
}
