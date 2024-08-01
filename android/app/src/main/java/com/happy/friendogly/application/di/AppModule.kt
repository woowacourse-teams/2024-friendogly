package com.happy.friendogly.application.di

import android.content.Context
import com.happy.friendogly.BuildConfig
import com.happy.friendogly.data.repository.AddressRepositoryImpl
import com.happy.friendogly.data.repository.ClubRepositoryImpl
import com.happy.friendogly.data.repository.FootprintRepositoryImpl
import com.happy.friendogly.data.repository.KakaoLoginRepositoryImpl
import com.happy.friendogly.data.repository.LocalRepositoryImpl
import com.happy.friendogly.data.repository.MemberRepositoryImpl
import com.happy.friendogly.data.repository.PetRepositoryImpl
import com.happy.friendogly.data.repository.WoofRepositoryImpl
import com.happy.friendogly.data.source.AddressDataSource
import com.happy.friendogly.data.source.ClubDataSource
import com.happy.friendogly.data.source.FootprintDataSource
import com.happy.friendogly.data.source.KakaoLoginDataSource
import com.happy.friendogly.data.source.LocalDataSource
import com.happy.friendogly.data.source.MemberDataSource
import com.happy.friendogly.data.source.PetDataSource
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.domain.repository.AddressRepository
import com.happy.friendogly.domain.repository.ClubRepository
import com.happy.friendogly.domain.repository.FootprintRepository
import com.happy.friendogly.domain.repository.KakaoLoginRepository
import com.happy.friendogly.domain.repository.LocalRepository
import com.happy.friendogly.domain.repository.MemberRepository
import com.happy.friendogly.domain.repository.PetRepository
import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.domain.usecase.DeleteAddressUseCase
import com.happy.friendogly.domain.usecase.DeleteClubUseCase
import com.happy.friendogly.domain.usecase.DeleteLocalDataUseCase
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.domain.usecase.GetClubMineUseCase
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.happy.friendogly.domain.usecase.GetJwtTokenUseCase
import com.happy.friendogly.domain.usecase.GetLandMarksUseCase
import com.happy.friendogly.domain.usecase.GetMemberMineUseCase
import com.happy.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.domain.usecase.KakaoLoginUseCase
import com.happy.friendogly.domain.usecase.PostClubParticipationUseCase
import com.happy.friendogly.domain.usecase.PostClubUseCase
import com.happy.friendogly.domain.usecase.PostFootprintUseCase
import com.happy.friendogly.domain.usecase.PostMemberUseCase
import com.happy.friendogly.domain.usecase.PostPetUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.happy.friendogly.kakao.source.KakaoLoginDataSourceImpl
import com.happy.friendogly.local.di.LocalModule
import com.happy.friendogly.local.di.AddressModule
import com.happy.friendogly.local.source.AddressDataSourceImpl
import com.happy.friendogly.local.source.LocalDataSourceImpl
import com.happy.friendogly.remote.api.BaseUrl
import com.happy.friendogly.remote.di.RemoteModule
import com.happy.friendogly.remote.source.ClubDataSourceImpl
import com.happy.friendogly.remote.source.FootprintDataSourceImpl
import com.happy.friendogly.remote.source.MemberDataSourceImpl
import com.happy.friendogly.remote.source.PetDataSourceImpl
import com.happy.friendogly.remote.source.WoofDataSourceImpl

class AppModule(context: Context) {
    private val baseUrl = BaseUrl(BuildConfig.base_url)

    private val localModule = LocalModule(context)
    private val addressModule = AddressModule(context)

    // service
    private val clubService =
        RemoteModule.createClubService(
            baseUrl = baseUrl,
            localModule = localModule,
        )
    private val footprintService =
        RemoteModule.createFootprintService(
            baseUrl = baseUrl,
            localModule = localModule,
        )
    private val woofService =
        RemoteModule.createWoofService(
            baseUrl = baseUrl,
            localModule = localModule,
        )

    private val memberService =
        RemoteModule.createMemberService(
            baseUrl = baseUrl,
            localModule = localModule,
        )

    private val petService =
        RemoteModule.createPetService(
            baseUrl = baseUrl,
            localModule = localModule,
        )

    // data source
    private val clubDataSource: ClubDataSource = ClubDataSourceImpl(service = clubService)
    private val localDataSource: LocalDataSource = LocalDataSourceImpl(localModule = localModule)
    private val addressDataSource: AddressDataSource = AddressDataSourceImpl(addressModule = addressModule)
    private val kakaoLoginDataSource: KakaoLoginDataSource = KakaoLoginDataSourceImpl()
    private val footprintDataSource: FootprintDataSource =
        FootprintDataSourceImpl(service = footprintService)
    private val woofDataSource: WoofDataSource = WoofDataSourceImpl(service = woofService)
    private val memberDataSource: MemberDataSource = MemberDataSourceImpl(service = memberService)
    private val petDataSource: PetDataSource = PetDataSourceImpl(service = petService)

    // repository
    private val clubRepository: ClubRepository = ClubRepositoryImpl(source = clubDataSource)
    private val localRepository: LocalRepository = LocalRepositoryImpl(source = localDataSource)
    private val kakaoLoginRepository: KakaoLoginRepository =
        KakaoLoginRepositoryImpl(dataSource = kakaoLoginDataSource)
    private val footprintRepository: FootprintRepository =
        FootprintRepositoryImpl(source = footprintDataSource)
    private val woofRepository: WoofRepository = WoofRepositoryImpl(source = woofDataSource)
    private val memberRepository: MemberRepository = MemberRepositoryImpl(source = memberDataSource)
    private val petRepository: PetRepository = PetRepositoryImpl(source = petDataSource)
    private val addressRepository: AddressRepository = AddressRepositoryImpl(addressDataSource = addressDataSource)

    // use case
    val kakaoLoginUseCase: KakaoLoginUseCase = KakaoLoginUseCase(repository = kakaoLoginRepository)
    val deleteClubUseCase: DeleteClubUseCase = DeleteClubUseCase(repository = clubRepository)
    val getClubMineUseCase: GetClubMineUseCase = GetClubMineUseCase(repository = clubRepository)
    val postClubParticipationUseCase: PostClubParticipationUseCase =
        PostClubParticipationUseCase(repository = clubRepository)
    val postClubUseCase: PostClubUseCase = PostClubUseCase(repository = clubRepository)
    val getJwtTokenUseCase: GetJwtTokenUseCase = GetJwtTokenUseCase(repository = localRepository)
    val saveJwtTokenUseCase: SaveJwtTokenUseCase = SaveJwtTokenUseCase(repository = localRepository)
    val deleteLocalDataUseCase: DeleteLocalDataUseCase =
        DeleteLocalDataUseCase(repository = localRepository)
    val getFootprintInfoUseCase: GetFootprintInfoUseCase =
        GetFootprintInfoUseCase(repository = footprintRepository)
    val postFootprintUseCase: PostFootprintUseCase =
        PostFootprintUseCase(repository = woofRepository)
    val getNearFootprintsUseCase: GetNearFootprintsUseCase =
        GetNearFootprintsUseCase(repository = woofRepository)
    val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase =
        GetFootprintMarkBtnInfoUseCase(repository = woofRepository)
    val getLandMarksUseCase: GetLandMarksUseCase =
        GetLandMarksUseCase(repository = woofRepository)
    val postMemberUseCase: PostMemberUseCase = PostMemberUseCase(repository = memberRepository)
    val getPetsMineUseCase: GetPetsMineUseCase = GetPetsMineUseCase(repository = petRepository)
    val postPetUseCase: PostPetUseCase = PostPetUseCase(repository = petRepository)
    val getMemberMineUseCase: GetMemberMineUseCase =
        GetMemberMineUseCase(repository = memberRepository)
    val getAddressUseCase: GetAddressUseCase = GetAddressUseCase(repository = addressRepository)
    val deleteAddressUseCase: DeleteAddressUseCase = DeleteAddressUseCase(repository = addressRepository)

    companion object {
        private var instance: AppModule? = null

        fun setInstance(context: Context) {
            instance = AppModule(context)
        }

        fun getInstance(): AppModule = requireNotNull(instance)
    }
}
