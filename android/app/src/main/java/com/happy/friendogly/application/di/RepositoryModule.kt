package com.happy.friendogly.application.di

import com.happy.friendogly.data.repository.AddressRepositoryImpl
import com.happy.friendogly.data.repository.AlarmSettingRepositoryImpl
import com.happy.friendogly.data.repository.AlarmTokenRepositoryImpl
import com.happy.friendogly.data.repository.AuthRepositoryImpl
import com.happy.friendogly.data.repository.ChatRoomRepositoryImpl
import com.happy.friendogly.data.repository.ClubRepositoryImpl
import com.happy.friendogly.data.repository.KakaoLoginRepositoryImpl
import com.happy.friendogly.data.repository.MemberRepositoryImpl
import com.happy.friendogly.data.repository.MessagingRepositoryImpl
import com.happy.friendogly.data.repository.MyClubRepositoryImpl
import com.happy.friendogly.data.repository.PetRepositoryImpl
import com.happy.friendogly.data.repository.TokenRepositoryImpl
import com.happy.friendogly.data.repository.WebSocketRepositoryImpl
import com.happy.friendogly.data.repository.WoofRepositoryImpl
import com.happy.friendogly.domain.repository.AddressRepository
import com.happy.friendogly.domain.repository.AlarmSettingRepository
import com.happy.friendogly.domain.repository.AlarmTokenRepository
import com.happy.friendogly.domain.repository.AuthRepository
import com.happy.friendogly.domain.repository.ChatRoomRepository
import com.happy.friendogly.domain.repository.ClubRepository
import com.happy.friendogly.domain.repository.KakaoLoginRepository
import com.happy.friendogly.domain.repository.MemberRepository
import com.happy.friendogly.domain.repository.MessagingRepository
import com.happy.friendogly.domain.repository.MyClubRepository
import com.happy.friendogly.domain.repository.PetRepository
import com.happy.friendogly.domain.repository.TokenRepository
import com.happy.friendogly.domain.repository.WebSocketRepository
import com.happy.friendogly.domain.repository.WoofRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsAddressRepository(repositoryImpl: AddressRepositoryImpl): AddressRepository

    @Binds
    @Singleton
    abstract fun bindsAlarmSettingRepository(repositoryImpl: AlarmSettingRepositoryImpl): AlarmSettingRepository

    @Binds
    @Singleton
    abstract fun bindsAlarmTokenRepository(repositoryImpl: AlarmTokenRepositoryImpl): AlarmTokenRepository

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsChatRoomRepository(repositoryImpl: ChatRoomRepositoryImpl): ChatRoomRepository

    @Binds
    @Singleton
    abstract fun bindsClubRepository(repositoryImpl: ClubRepositoryImpl): ClubRepository

    @Binds
    @Singleton
    abstract fun bindsKakaoLoginRepository(repositoryImpl: KakaoLoginRepositoryImpl): KakaoLoginRepository

    @Binds
    @Singleton
    abstract fun bindsMemberRepository(repositoryImpl: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindsMessagingRepository(repositoryImpl: MessagingRepositoryImpl): MessagingRepository

    @Binds
    @Singleton
    abstract fun bindsMyClubRepository(repositoryImpl: MyClubRepositoryImpl): MyClubRepository

    @Binds
    @Singleton
    abstract fun bindsPetRepository(repositoryImpl: PetRepositoryImpl): PetRepository

    @Binds
    @Singleton
    abstract fun bindsTokenRepository(repositoryImpl: TokenRepositoryImpl): TokenRepository

    @Binds
    @Singleton
    abstract fun bindsWebSocketRepository(repositoryImpl: WebSocketRepositoryImpl): WebSocketRepository

    @Binds
    @Singleton
    abstract fun bindsWoofRepository(repositoryImpl: WoofRepositoryImpl): WoofRepository
}
