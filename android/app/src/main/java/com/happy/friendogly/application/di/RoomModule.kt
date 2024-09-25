package com.happy.friendogly.application.di

import android.content.Context
import androidx.room.Room
import com.happy.friendogly.local.room.ChatMessageDao
import com.happy.friendogly.local.room.ChatMessageDatabase
import com.happy.friendogly.local.room.ChatRoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideChatRoomDatabase(
        @ApplicationContext appContext: Context,
    ): ChatMessageDatabase =
        Room.databaseBuilder(
            appContext,
            ChatMessageDatabase::class.java,
            "chat",
        ).build()

    @Provides
    @Singleton
    fun provideChatRoomDao(chatMessageDatabase: ChatMessageDatabase): ChatRoomDao = chatMessageDatabase.chatRoomDao()

    @Provides
    @Singleton
    fun provideChatMessageDao(chatMessageDatabase: ChatMessageDatabase): ChatMessageDao = chatMessageDatabase.chatMessageDao()
}
