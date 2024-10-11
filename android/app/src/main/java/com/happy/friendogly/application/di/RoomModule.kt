package com.happy.friendogly.application.di

import android.content.Context
import androidx.room.Room
import com.happy.friendogly.local.dao.ChatMessageDao
import com.happy.friendogly.local.dao.RecentPetsDao
import com.happy.friendogly.local.room.ChatMessageDatabase
import com.happy.friendogly.local.room.RecentPetsDatabase
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
    fun providesChatRoomDatabase(
        @ApplicationContext appContext: Context,
    ): ChatMessageDatabase =
        Room
            .databaseBuilder(
                appContext,
                ChatMessageDatabase::class.java,
                "chat",
            ).build()

    @Provides
    @Singleton
    fun providesChatMessageDao(chatMessageDatabase: ChatMessageDatabase): ChatMessageDao = chatMessageDatabase.chatMessageDao()

    @Provides
    @Singleton
    fun providesRecentPetsDatabase(
        @ApplicationContext appContext: Context,
    ): RecentPetsDatabase =
        Room
            .databaseBuilder(
                appContext,
                RecentPetsDatabase::class.java,
                "recent_pets",
            ).build()

    @Provides
    @Singleton
    fun providesRecentPetDao(database: RecentPetsDatabase): RecentPetsDao = database.recentPetDao()
}
