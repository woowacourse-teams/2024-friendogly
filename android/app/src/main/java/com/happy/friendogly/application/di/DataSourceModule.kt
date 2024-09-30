package com.happy.friendogly.application.di

import com.happy.friendogly.data.source.AddressDataSource
import com.happy.friendogly.data.source.AlarmSettingDataSource
import com.happy.friendogly.data.source.AlarmTokenDataSource
import com.happy.friendogly.data.source.AuthDataSource
import com.happy.friendogly.data.source.ChatDataSource
import com.happy.friendogly.data.source.ClubDataSource
import com.happy.friendogly.data.source.KakaoLoginDataSource
import com.happy.friendogly.data.source.MemberDataSource
import com.happy.friendogly.data.source.MessagingDataSource
import com.happy.friendogly.data.source.MyClubDataSource
import com.happy.friendogly.data.source.PetDataSource
import com.happy.friendogly.data.source.RecentPetsDataSource
import com.happy.friendogly.data.source.TokenDataSource
import com.happy.friendogly.data.source.WebSocketDataSource
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.firebase.source.MessagingDataSourceImpl
import com.happy.friendogly.kakao.source.KakaoLoginDataSourceImpl
import com.happy.friendogly.local.source.AddressDataSourceImpl
import com.happy.friendogly.local.source.AlarmSettingDataSourceImpl
import com.happy.friendogly.local.source.RecentPetsDataSourceImpl
import com.happy.friendogly.local.source.TokenDataSourceImpl
import com.happy.friendogly.remote.source.AlamTokenDataSourceImpl
import com.happy.friendogly.remote.source.AuthDataSourceImpl
import com.happy.friendogly.remote.source.ChatDataSourceImpl
import com.happy.friendogly.remote.source.ClubDataSourceImpl
import com.happy.friendogly.remote.source.MemberDataSourceImpl
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
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsAddressDataSource(dataSourceImpl: AddressDataSourceImpl): AddressDataSource

    @Binds
    @Singleton
    abstract fun bindsAlarmSettingDataSource(dataSourceImpl: AlarmSettingDataSourceImpl): AlarmSettingDataSource

    @Binds
    @Singleton
    abstract fun bindsAlarmTokenDataSource(dataSourceImpl: AlamTokenDataSourceImpl): AlarmTokenDataSource

    @Binds
    @Singleton
    abstract fun bindsAuthDataSource(dataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindsChatDataSource(dataSourceImpl: ChatDataSourceImpl): ChatDataSource

    @Binds
    @Singleton
    abstract fun bindsClubDataSource(dataSourceImpl: ClubDataSourceImpl): ClubDataSource

    @Binds
    @Singleton
    abstract fun bindsKakaoLoginDataSource(dataSourceImpl: KakaoLoginDataSourceImpl): KakaoLoginDataSource

    @Binds
    @Singleton
    abstract fun bindsMemberDataSource(dataSourceImpl: MemberDataSourceImpl): MemberDataSource

    @Binds
    @Singleton
    abstract fun bindsMessagingDataSource(dataSourceImpl: MessagingDataSourceImpl): MessagingDataSource

    @Binds
    @Singleton
    abstract fun bindsMyClubDataSource(dataSourceImpl: MyClubDataSourceImpl): MyClubDataSource

    @Binds
    @Singleton
    abstract fun bindsPetDataSource(dataSourceImpl: PetDataSourceImpl): PetDataSource

    @Binds
    @Singleton
    abstract fun bindsTokenDataSource(dataSourceImpl: TokenDataSourceImpl): TokenDataSource

    @Binds
    @Singleton
    abstract fun bindsWebSocketDataSource(dataSourceImpl: WebSocketDataSourceImpl): WebSocketDataSource

    @Binds
    @Singleton
    abstract fun bindsWoofDataSource(dataSourceImpl: WoofDataSourceImpl): WoofDataSource

    @Binds
    @Singleton
    abstract fun bindsRecentPetsDataSource(dataSourceImpl: RecentPetsDataSourceImpl): RecentPetsDataSource
}
