package com.happy.friendogly.application.di

import android.content.Context
import com.happy.friendogly.local.di.AddressModule
import com.happy.friendogly.local.di.AlarmTokenModule
import com.happy.friendogly.local.di.ChatAlarmModule
import com.happy.friendogly.local.di.TokenManager
import com.happy.friendogly.local.di.WoofAlarmModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesAddress(
        @ApplicationContext appContext: Context,
    ): AddressModule = AddressModule(appContext)

    @Provides
    @Singleton
    fun providesAlarmTokenModule(
        @ApplicationContext appContext: Context,
    ): AlarmTokenModule = AlarmTokenModule(appContext)

    @Provides
    @Singleton
    fun providesChatAlarmModule(
        @ApplicationContext appContext: Context,
    ): ChatAlarmModule = ChatAlarmModule(appContext)

    @Provides
    @Singleton
    fun providesTokenManager(
        @ApplicationContext appContext: Context,
    ): TokenManager = TokenManager(appContext)

    @Provides
    @Singleton
    fun providesWoofAlarmModule(
        @ApplicationContext appContext: Context,
    ): WoofAlarmModule = WoofAlarmModule(appContext)
}
