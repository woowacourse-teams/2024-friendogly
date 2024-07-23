package com.woowacourse.friendogly.application.di

import android.content.Context
import com.woowacourse.friendogly.BuildConfig
import com.woowacourse.friendogly.data.repository.ClubRepositoryImpl
import com.woowacourse.friendogly.data.repository.FootprintRepositoryImpl
import com.woowacourse.friendogly.data.repository.KakaoLoginRepositoryImpl
import com.woowacourse.friendogly.data.repository.LocalRepositoryImpl
import com.woowacourse.friendogly.data.repository.WoofRepositoryImpl
import com.woowacourse.friendogly.data.repository.MemberRepositoryImpl
import com.woowacourse.friendogly.data.repository.PetRepositoryImpl
import com.woowacourse.friendogly.data.source.ClubDataSource
import com.woowacourse.friendogly.data.source.FootprintDataSource
import com.woowacourse.friendogly.data.source.KakaoLoginDataSource
import com.woowacourse.friendogly.data.source.LocalDataSource
import com.woowacourse.friendogly.data.source.WoofDataSource
import com.woowacourse.friendogly.data.source.MemberDataSource
import com.woowacourse.friendogly.data.source.PetDataSource
import com.woowacourse.friendogly.domain.repository.ClubRepository
import com.woowacourse.friendogly.domain.repository.FootprintRepository
import com.woowacourse.friendogly.domain.repository.KakaoLoginRepository
import com.woowacourse.friendogly.domain.repository.LocalRepository
import com.woowacourse.friendogly.domain.repository.WoofRepository
import com.woowacourse.friendogly.domain.repository.MemberRepository
import com.woowacourse.friendogly.domain.repository.PetRepository
import com.woowacourse.friendogly.domain.usecase.DeleteClubUseCase
import com.woowacourse.friendogly.domain.usecase.DeleteLocalDataUseCase
import com.woowacourse.friendogly.domain.usecase.GetClubMineUseCase
import com.woowacourse.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.woowacourse.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.woowacourse.friendogly.domain.usecase.GetJwtTokenUseCase
import com.woowacourse.friendogly.domain.usecase.GetMemberMineUseCase
import com.woowacourse.friendogly.domain.usecase.GetLandMarksUseCase
import com.woowacourse.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.woowacourse.friendogly.domain.usecase.GetPetsMineUseCase
import com.woowacourse.friendogly.domain.usecase.KakaoLoginUseCase
import com.woowacourse.friendogly.domain.usecase.PostClubParticipationUseCase
import com.woowacourse.friendogly.domain.usecase.PostClubUseCase
import com.woowacourse.friendogly.domain.usecase.PostFootprintUseCase
import com.woowacourse.friendogly.domain.usecase.PostMemberUseCase
import com.woowacourse.friendogly.domain.usecase.PostPetUseCase
import com.woowacourse.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.woowacourse.friendogly.kakao.source.KakaoLoginDataSourceImpl
import com.woowacourse.friendogly.local.di.LocalModule
import com.woowacourse.friendogly.local.source.LocalDataSourceImpl
import com.woowacourse.friendogly.remote.api.BaseUrl
import com.woowacourse.friendogly.remote.di.RemoteModule
import com.woowacourse.friendogly.remote.source.ClubDataSourceImpl
import com.woowacourse.friendogly.remote.source.MemberDataSourceImpl
import com.woowacourse.friendogly.remote.source.PetDataSourceImpl
import com.woowacourse.friendogly.remote.source.FootprintDataSourceImpl
import com.woowacourse.friendogly.remote.source.WoofDataSourceImpl

class AppModule(context: Context) {
    private val baseUrl = BaseUrl(BuildConfig.base_url)

    private val localModule = LocalModule(context)

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

    companion object {
        private var instance: AppModule? = null

        fun setInstance(context: Context) {
            instance = AppModule(context)
        }

        fun getInstance(): AppModule = requireNotNull(instance)
    }
}
