package com.happy.friendogly.application.di

import com.happy.friendogly.data.repository.AddressRepositoryImpl
import com.happy.friendogly.data.repository.AlarmSettingRepositoryImpl
import com.happy.friendogly.data.repository.AuthRepositoryImpl
import com.happy.friendogly.data.repository.ChatRepositoryImpl
import com.happy.friendogly.data.repository.ClubRepositoryImpl
import com.happy.friendogly.data.repository.KakaoLoginRepositoryImpl
import com.happy.friendogly.data.repository.MemberRepositoryImpl
import com.happy.friendogly.data.repository.MessagingRepositoryImpl
import com.happy.friendogly.data.repository.MyClubRepositoryImpl
import com.happy.friendogly.data.repository.PetRepositoryImpl
import com.happy.friendogly.data.repository.TokenRepositoryImpl
import com.happy.friendogly.data.repository.WebSocketRepositoryImpl
import com.happy.friendogly.data.repository.WoofRepositoryImpl
import com.happy.friendogly.data.source.AlarmSettingDataSource
import com.happy.friendogly.data.source.AuthDataSource
import com.happy.friendogly.data.source.ChatDataSource
import com.happy.friendogly.data.source.ClubDataSource
import com.happy.friendogly.data.source.KakaoLoginDataSource
import com.happy.friendogly.data.source.MessagingDataSource
import com.happy.friendogly.data.source.MyClubDataSource
import com.happy.friendogly.data.source.PetDataSource
import com.happy.friendogly.data.source.TokenDataSource
import com.happy.friendogly.data.source.WebSocketDataSource
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.domain.repository.AddressRepository
import com.happy.friendogly.domain.repository.AlarmSettingRepository
import com.happy.friendogly.domain.repository.AuthRepository
import com.happy.friendogly.domain.repository.ChatRepository
import com.happy.friendogly.domain.repository.ClubRepository
import com.happy.friendogly.domain.repository.KakaoLoginRepository
import com.happy.friendogly.domain.repository.MemberRepository
import com.happy.friendogly.domain.repository.MessagingRepository
import com.happy.friendogly.domain.repository.MyClubRepository
import com.happy.friendogly.domain.repository.PetRepository
import com.happy.friendogly.domain.repository.TokenRepository
import com.happy.friendogly.domain.repository.WebSocketRepository
import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.firebase.source.MessagingDataSourceImpl
import com.happy.friendogly.kakao.source.KakaoLoginDataSourceImpl
import com.happy.friendogly.local.source.AddressDataSourceImpl
import com.happy.friendogly.local.source.AlarmSettingDataSourceImpl
import com.happy.friendogly.local.source.TokenDataSourceImpl
import com.happy.friendogly.remote.source.AuthDataSourceImpl
import com.happy.friendogly.remote.source.ChatDataSourceImpl
import com.happy.friendogly.remote.source.ClubDataSourceImpl
import com.happy.friendogly.remote.source.MyClubDataSourceImpl
import com.happy.friendogly.remote.source.PetDataSourceImpl
import com.happy.friendogly.remote.source.WebSocketDataSourceImpl
import com.happy.friendogly.remote.source.WoofDataSourceImpl
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
    abstract fun provideAddressRepository(repositoryImpl: AddressRepositoryImpl): AddressRepository

    @Binds
    @Singleton
    abstract fun provideAlarmSettingRepository(repositoryImpl: AlarmSettingRepositoryImpl): AlarmSettingRepository

    @Binds
    @Singleton
    abstract fun provideAlarmTokenRepository(repositoryImpl: AlarmSettingRepositoryImpl): AlarmSettingRepository

    @Binds
    @Singleton
    abstract fun provideAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun provideChatRepository(repositoryImpl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun provideClubRepository(repositoryImpl: ClubRepositoryImpl): ClubRepository

    @Binds
    @Singleton
    abstract fun provideKakaoLoginRepository(repositoryImpl: KakaoLoginRepositoryImpl): KakaoLoginRepository

    @Binds
    @Singleton
    abstract fun provideMemberRepository(repositoryImpl: MemberRepositoryImpl):MemberRepository

    @Binds
    @Singleton
    abstract fun provideMessagingRepository(repositoryImpl: MessagingRepositoryImpl): MessagingRepository

    @Binds
    @Singleton
    abstract fun provideMyClubRepository(repositoryImpl: MyClubRepositoryImpl): MyClubRepository

    @Binds
    @Singleton
    abstract fun providePetRepository(repositoryImpl: PetRepositoryImpl): PetRepository

    @Binds
    @Singleton
    abstract fun provideTokenRepository(repositoryImpl: TokenRepositoryImpl): TokenRepository

    @Binds
    @Singleton
    abstract fun provideWebSocketRepository(repositoryImpl: WebSocketRepositoryImpl): WebSocketRepository

    @Binds
    @Singleton
    abstract fun provideWoofRepository(repositoryImpl: WoofRepositoryImpl): WoofRepository

}