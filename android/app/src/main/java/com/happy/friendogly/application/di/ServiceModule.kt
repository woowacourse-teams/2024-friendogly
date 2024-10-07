package com.happy.friendogly.application.di

import com.happy.friendogly.remote.api.AlarmTokenService
import com.happy.friendogly.remote.api.ChatService
import com.happy.friendogly.remote.api.ClubService
import com.happy.friendogly.remote.api.MemberService
import com.happy.friendogly.remote.api.MyClubService
import com.happy.friendogly.remote.api.PetService
import com.happy.friendogly.remote.api.WoofService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providesAlarmTokenService(retrofit: Retrofit): AlarmTokenService =
        retrofit.create(
            AlarmTokenService::class.java,
        )

    @Provides
    @Singleton
    fun providesChatService(retrofit: Retrofit): ChatService = retrofit.create(ChatService::class.java)

    @Provides
    @Singleton
    fun providesClubService(retrofit: Retrofit): ClubService = retrofit.create(ClubService::class.java)

    @Provides
    @Singleton
    fun providesMemberService(retrofit: Retrofit): MemberService = retrofit.create(MemberService::class.java)

    @Provides
    @Singleton
    fun providesMyClubService(retrofit: Retrofit): MyClubService = retrofit.create(MyClubService::class.java)

    @Provides
    @Singleton
    fun providesPerService(retrofit: Retrofit): PetService = retrofit.create(PetService::class.java)

    @Provides
    @Singleton
    fun providesWoofService(retrofit: Retrofit): WoofService = retrofit.create(WoofService::class.java)
}