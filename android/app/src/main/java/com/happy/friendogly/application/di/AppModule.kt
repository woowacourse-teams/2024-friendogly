package com.happy.friendogly.application.di

import android.content.Context
import androidx.room.Room
import com.happy.friendogly.BuildConfig
import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.crashlytics.CrashlyticsHelper
import com.happy.friendogly.data.repository.AddressRepositoryImpl
import com.happy.friendogly.data.repository.AlarmSettingRepositoryImpl
import com.happy.friendogly.data.repository.AlarmTokenRepositoryImpl
import com.happy.friendogly.data.repository.AuthRepositoryImpl
import com.happy.friendogly.data.repository.ChatRepositoryImpl
import com.happy.friendogly.data.repository.ClubRepositoryImpl
import com.happy.friendogly.data.repository.KakaoLoginRepositoryImpl
import com.happy.friendogly.data.repository.MemberRepositoryImpl
import com.happy.friendogly.data.repository.MyClubRepositoryImpl
import com.happy.friendogly.data.repository.PetRepositoryImpl
import com.happy.friendogly.data.repository.TokenRepositoryImpl
import com.happy.friendogly.data.repository.WebSocketRepositoryImpl
import com.happy.friendogly.data.repository.WoofRepositoryImpl
import com.happy.friendogly.data.source.AddressDataSource
import com.happy.friendogly.data.source.AlarmSettingDataSource
import com.happy.friendogly.data.source.AlarmTokenDataSource
import com.happy.friendogly.data.source.AuthDataSource
import com.happy.friendogly.data.source.ChatDataSource
import com.happy.friendogly.data.source.ClubDataSource
import com.happy.friendogly.data.source.KakaoLoginDataSource
import com.happy.friendogly.data.source.MemberDataSource
import com.happy.friendogly.data.source.MyClubDataSource
import com.happy.friendogly.data.source.PetDataSource
import com.happy.friendogly.data.source.TokenDataSource
import com.happy.friendogly.data.source.WebSocketDataSource
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.domain.repository.AddressRepository
import com.happy.friendogly.domain.repository.AlarmSettingRepository
import com.happy.friendogly.domain.repository.AlarmTokenRepository
import com.happy.friendogly.domain.repository.AuthRepository
import com.happy.friendogly.domain.repository.ChatRepository
import com.happy.friendogly.domain.repository.ClubRepository
import com.happy.friendogly.domain.repository.KakaoLoginRepository
import com.happy.friendogly.domain.repository.MemberRepository
import com.happy.friendogly.domain.repository.MyClubRepository
import com.happy.friendogly.domain.repository.PetRepository
import com.happy.friendogly.domain.repository.TokenRepository
import com.happy.friendogly.domain.repository.WebSocketRepository
import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.domain.usecase.ConnectWebsocketUseCase
import com.happy.friendogly.domain.usecase.DeleteAddressUseCase
import com.happy.friendogly.domain.usecase.DeleteChatAlarmUseCase
import com.happy.friendogly.domain.usecase.DeleteClubMemberUseCase
import com.happy.friendogly.domain.usecase.DeleteTokenUseCase
import com.happy.friendogly.domain.usecase.DeleteWoofAlarmUseCase
import com.happy.friendogly.domain.usecase.DisconnectWebsocketUseCase
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.domain.usecase.GetChatAlarmUseCase
import com.happy.friendogly.domain.usecase.GetChatListUseCase
import com.happy.friendogly.domain.usecase.GetChatMemberUseCase
import com.happy.friendogly.domain.usecase.GetChatMessagesUseCase
import com.happy.friendogly.domain.usecase.GetChatRoomClubUseCase
import com.happy.friendogly.domain.usecase.GetClubUseCase
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.happy.friendogly.domain.usecase.GetJwtTokenUseCase
import com.happy.friendogly.domain.usecase.GetMemberMineUseCase
import com.happy.friendogly.domain.usecase.GetMemberUseCase
import com.happy.friendogly.domain.usecase.GetMyClubUseCase
import com.happy.friendogly.domain.usecase.GetMyHeadClubUseCase
import com.happy.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.domain.usecase.GetPetsUseCase
import com.happy.friendogly.domain.usecase.GetSearchingClubsUseCase
import com.happy.friendogly.domain.usecase.GetWoofAlarmUseCase
import com.happy.friendogly.domain.usecase.KakaoLoginUseCase
import com.happy.friendogly.domain.usecase.PatchClubUseCase
import com.happy.friendogly.domain.usecase.PatchMemberUseCase
import com.happy.friendogly.domain.usecase.PatchPetUseCase
import com.happy.friendogly.domain.usecase.PatchWalkStatusUseCase
import com.happy.friendogly.domain.usecase.PostClubMemberUseCase
import com.happy.friendogly.domain.usecase.PostClubUseCase
import com.happy.friendogly.domain.usecase.PostFootprintUseCase
import com.happy.friendogly.domain.usecase.PostKakaoLoginUseCase
import com.happy.friendogly.domain.usecase.PostLogoutUseCase
import com.happy.friendogly.domain.usecase.PostMemberUseCase
import com.happy.friendogly.domain.usecase.PostPetUseCase
import com.happy.friendogly.domain.usecase.PublishEnterUseCase
import com.happy.friendogly.domain.usecase.PublishLeaveUseCase
import com.happy.friendogly.domain.usecase.PublishSendMessageUseCase
import com.happy.friendogly.domain.usecase.SaveAddressUseCase
import com.happy.friendogly.domain.usecase.SaveAlamTokenUseCase
import com.happy.friendogly.domain.usecase.SaveChatAlarmUseCase
import com.happy.friendogly.domain.usecase.SaveChatMessageUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.happy.friendogly.domain.usecase.SaveWoofAlarmUseCase
import com.happy.friendogly.domain.usecase.SubScribeMessageUseCase
import com.happy.friendogly.kakao.source.KakaoLoginDataSourceImpl
import com.happy.friendogly.local.di.AddressModule
import com.happy.friendogly.local.di.AlarmTokenModule
import com.happy.friendogly.local.di.ChatAlarmModule
import com.happy.friendogly.local.di.TokenManager
import com.happy.friendogly.local.di.WoofAlarmModule
import com.happy.friendogly.local.room.ChatMessageDatabase
import com.happy.friendogly.local.source.AddressDataSourceImpl
import com.happy.friendogly.local.source.AlarmSettingDataSourceImpl
import com.happy.friendogly.local.source.TokenDataSourceImpl
import com.happy.friendogly.remote.api.AuthenticationListener
import com.happy.friendogly.remote.api.BaseUrl
import com.happy.friendogly.remote.api.WebSocketService
import com.happy.friendogly.remote.di.RemoteModule
import com.happy.friendogly.remote.source.AlamTokenDataSourceImpl
import com.happy.friendogly.remote.source.AuthDataSourceImpl
import com.happy.friendogly.remote.source.ChatDataSourceImpl
import com.happy.friendogly.remote.source.ClubDataSourceImpl
import com.happy.friendogly.remote.source.MemberDataSourceImpl
import com.happy.friendogly.remote.source.MyClubDataSourceImpl
import com.happy.friendogly.remote.source.PetDataSourceImpl
import com.happy.friendogly.remote.source.WebSocketDataSourceImpl
import com.happy.friendogly.remote.source.WoofDataSourceImpl

class AppModule(context: Context) {
    // analytics & crashlytics
    val analyticsHelper = AnalyticsHelper(context)
    val crashlyticsHelper = CrashlyticsHelper()

    private val baseUrl = BaseUrl(BuildConfig.base_url)
    private val websocketUrl = BaseUrl(BuildConfig.websocket_url)

    private val tokenManager = TokenManager(context)
    private val authenticationListener: AuthenticationListener =
        AuthenticationListenerImpl(context, tokenManager)
    private val addressModule = AddressModule(context)
    private val chatAlarmModule = ChatAlarmModule(context)
    private val woofAlarmModule = WoofAlarmModule(context)
    private val alarmTokenModule = AlarmTokenModule(context)
    private val chatDataBase =
        Room.databaseBuilder(context, ChatMessageDatabase::class.java, "chat").build()
    private val chatRoomDao = chatDataBase.chatRoomDao()

    // service
    private val authService =
        RemoteModule.createAuthService(
            baseUrl = baseUrl,
            tokenManager = tokenManager,
            authenticationListener = authenticationListener,
        )

    private val clubService =
        RemoteModule.createClubService(
            baseUrl = baseUrl,
            tokenManager = tokenManager,
            authenticationListener = authenticationListener,
        )

    private val myClubService =
        RemoteModule.createMyClubService(
            baseUrl = baseUrl,
            tokenManager = tokenManager,
            authenticationListener = authenticationListener,
        )

    private val woofService =
        RemoteModule.createWoofService(
            baseUrl = baseUrl,
            tokenManager = tokenManager,
            authenticationListener = authenticationListener,
        )

    private val memberService =
        RemoteModule.createMemberService(
            baseUrl = baseUrl,
            tokenManager = tokenManager,
            authenticationListener = authenticationListener,
        )

    private val petService =
        RemoteModule.createPetService(
            baseUrl = baseUrl,
            tokenManager = tokenManager,
            authenticationListener = authenticationListener,
        )

    private val webSocketService =
        WebSocketService(
            client =
                RemoteModule.createStumpClient(
                    baseUrl = baseUrl,
                    tokenManager = tokenManager,
                    authenticationListener = authenticationListener,
                ),
            tokenManager = tokenManager,
            baseUrl = websocketUrl,
        )

    private val chatService =
        RemoteModule.createChatService(
            baseUrl = baseUrl,
            tokenManager = tokenManager,
            authenticationListener = authenticationListener,
        )

    private val alarmTokenService =
        RemoteModule.createAlarmTokenService(
            baseUrl = baseUrl,
            tokenManager = tokenManager,
            authenticationListener = authenticationListener,
        )

    // data source
    private val authDataSource: AuthDataSource = AuthDataSourceImpl(service = authService)
    private val clubDataSource: ClubDataSource = ClubDataSourceImpl(service = clubService)
    private val myClubDataSource: MyClubDataSource = MyClubDataSourceImpl(service = myClubService)
    private val tokenDataSource: TokenDataSource = TokenDataSourceImpl(tokenManager = tokenManager)
    private val addressDataSource: AddressDataSource =
        AddressDataSourceImpl(addressModule = addressModule)
    private val kakaoLoginDataSource: KakaoLoginDataSource = KakaoLoginDataSourceImpl()
    private val woofDataSource: WoofDataSource = WoofDataSourceImpl(service = woofService)
    private val memberDataSource: MemberDataSource = MemberDataSourceImpl(service = memberService)
    private val petDataSource: PetDataSource = PetDataSourceImpl(service = petService)
    private val webSocketDataSource: WebSocketDataSource =
        WebSocketDataSourceImpl(service = webSocketService)
    private val chatDataSource: ChatDataSource = ChatDataSourceImpl(service = chatService)
    private val alarmSettingDataSource: AlarmSettingDataSource =
        AlarmSettingDataSourceImpl(
            chatAlarmModule = chatAlarmModule,
            woofAlarmModule = woofAlarmModule,
        )
    private val alarmTokenDataSource: AlarmTokenDataSource =
        AlamTokenDataSourceImpl(service = alarmTokenService)

    // repository
    private val authRepository: AuthRepository = AuthRepositoryImpl(source = authDataSource)
    private val clubRepository: ClubRepository = ClubRepositoryImpl(source = clubDataSource)
    private val myClubRepository: MyClubRepository = MyClubRepositoryImpl(source = myClubDataSource)
    private val tokenRepository: TokenRepository = TokenRepositoryImpl(source = tokenDataSource)
    private val kakaoLoginRepository: KakaoLoginRepository =
        KakaoLoginRepositoryImpl(dataSource = kakaoLoginDataSource)
    private val woofRepository: WoofRepository = WoofRepositoryImpl(source = woofDataSource)
    private val memberRepository: MemberRepository = MemberRepositoryImpl(source = memberDataSource)
    private val petRepository: PetRepository = PetRepositoryImpl(source = petDataSource)
    private val addressRepository: AddressRepository =
        AddressRepositoryImpl(addressDataSource = addressDataSource)
    private val webSocketRepository: WebSocketRepository =
        WebSocketRepositoryImpl(source = webSocketDataSource)
    private val chatRepository: ChatRepository =
        ChatRepositoryImpl(source = chatDataSource, chatRoomDao = chatRoomDao)
    private val alarmSettingRepository: AlarmSettingRepository =
        AlarmSettingRepositoryImpl(source = alarmSettingDataSource)
    private val alarmTokenRepository: AlarmTokenRepository =
        AlarmTokenRepositoryImpl(source = alarmTokenDataSource)

    // use case
    val postKakaoLoginUseCase: PostKakaoLoginUseCase =
        PostKakaoLoginUseCase(repository = authRepository)
    val kakaoLoginUseCase: KakaoLoginUseCase = KakaoLoginUseCase(repository = kakaoLoginRepository)
    val postClubUseCase: PostClubUseCase = PostClubUseCase(repository = clubRepository)
    val getSearchingClubsUseCase: GetSearchingClubsUseCase =
        GetSearchingClubsUseCase(repository = clubRepository)
    val getClubUseCase: GetClubUseCase = GetClubUseCase(repository = clubRepository)
    val postClubMemberUseCase: PostClubMemberUseCase =
        PostClubMemberUseCase(repository = clubRepository)
    val deleteClubMemberUseCase: DeleteClubMemberUseCase =
        DeleteClubMemberUseCase(repository = clubRepository)
    val patchClubUseCase: PatchClubUseCase =
        PatchClubUseCase(repository = clubRepository)
    val getMyClubsUseCase: GetMyClubUseCase =
        GetMyClubUseCase(repository = myClubRepository)
    val getMyHeadClubsUseCase: GetMyHeadClubUseCase =
        GetMyHeadClubUseCase(repository = myClubRepository)
    val getJwtTokenUseCase: GetJwtTokenUseCase = GetJwtTokenUseCase(repository = tokenRepository)
    val saveJwtTokenUseCase: SaveJwtTokenUseCase = SaveJwtTokenUseCase(repository = tokenRepository)
    val deleteTokenUseCase: DeleteTokenUseCase =
        DeleteTokenUseCase(repository = tokenRepository)
    val getFootprintInfoUseCase: GetFootprintInfoUseCase =
        GetFootprintInfoUseCase(repository = woofRepository)
    val postFootprintUseCase: PostFootprintUseCase =
        PostFootprintUseCase(repository = woofRepository)
    val patchWalkStatusUseCase: PatchWalkStatusUseCase =
        PatchWalkStatusUseCase(repository = woofRepository)
    val getNearFootprintsUseCase: GetNearFootprintsUseCase =
        GetNearFootprintsUseCase(repository = woofRepository)
    val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase =
        GetFootprintMarkBtnInfoUseCase(repository = woofRepository)
    val postMemberUseCase: PostMemberUseCase = PostMemberUseCase(repository = memberRepository)
    val getPetsMineUseCase: GetPetsMineUseCase = GetPetsMineUseCase(repository = petRepository)
    val getPetsUseCase: GetPetsUseCase = GetPetsUseCase(repository = petRepository)
    val postPetUseCase: PostPetUseCase = PostPetUseCase(repository = petRepository)
    val getMemberMineUseCase: GetMemberMineUseCase =
        GetMemberMineUseCase(repository = memberRepository)
    val getMemberUseCase: GetMemberUseCase = GetMemberUseCase(repository = memberRepository)
    val getAddressUseCase: GetAddressUseCase = GetAddressUseCase(repository = addressRepository)
    val saveAddressUseCase: SaveAddressUseCase = SaveAddressUseCase(repository = addressRepository)
    val deleteAddressUseCase: DeleteAddressUseCase =
        DeleteAddressUseCase(repository = addressRepository)
    val getChatListUseCase: GetChatListUseCase = GetChatListUseCase(repository = chatRepository)
    val getChatMemberUseCase: GetChatMemberUseCase =
        GetChatMemberUseCase(repository = chatRepository)
    val saveChatMessageUseCase: SaveChatMessageUseCase =
        SaveChatMessageUseCase(repository = chatRepository)
    val getChatMessagesUseCase: GetChatMessagesUseCase =
        GetChatMessagesUseCase(repository = chatRepository)
    val getChatRoomClubUseCase: GetChatRoomClubUseCase =
        GetChatRoomClubUseCase(repository = chatRepository)
    val publishEnterUseCase: PublishEnterUseCase =
        PublishEnterUseCase(repository = webSocketRepository)
    val publishSendUseCase: PublishSendMessageUseCase =
        PublishSendMessageUseCase(repository = webSocketRepository)
    val publishLeaveUseCase: PublishLeaveUseCase =
        PublishLeaveUseCase(repository = webSocketRepository)
    val subScribeMessageUseCase: SubScribeMessageUseCase =
        SubScribeMessageUseCase(repository = webSocketRepository)
    val connectWebsocketUseCase: ConnectWebsocketUseCase =
        ConnectWebsocketUseCase(repository = webSocketRepository)
    val disconnectWebsocketUseCase: DisconnectWebsocketUseCase =
        DisconnectWebsocketUseCase(repository = webSocketRepository)
    val deleteChatAlarmUseCase: DeleteChatAlarmUseCase =
        DeleteChatAlarmUseCase(repository = alarmSettingRepository)
    val saveChatAlarmUseCase: SaveChatAlarmUseCase =
        SaveChatAlarmUseCase(repository = alarmSettingRepository)
    val saveWoofAlarmUseCase: SaveWoofAlarmUseCase =
        SaveWoofAlarmUseCase(repository = alarmSettingRepository)
    val getWoofAlarmUseCase: GetWoofAlarmUseCase =
        GetWoofAlarmUseCase(repository = alarmSettingRepository)
    val deleteWoofAlarmUseCase: DeleteWoofAlarmUseCase =
        DeleteWoofAlarmUseCase(repository = alarmSettingRepository)
    val getChatAlarmUseCase: GetChatAlarmUseCase =
        GetChatAlarmUseCase(repository = alarmSettingRepository)
    val saveAlarmTokenUseCase: SaveAlamTokenUseCase =
        SaveAlamTokenUseCase(repository = alarmTokenRepository)
    val postLogoutUseCase: PostLogoutUseCase = PostLogoutUseCase(repository = authRepository)
    val patchPetUseCase: PatchPetUseCase = PatchPetUseCase(repository = petRepository)
    val patchMemberUseCase: PatchMemberUseCase = PatchMemberUseCase(repository = memberRepository)

    companion object {
        private var instance: AppModule? = null

        fun setInstance(context: Context) {
            instance = AppModule(context)
        }

        fun getInstance(): AppModule = requireNotNull(instance)
    }
}
