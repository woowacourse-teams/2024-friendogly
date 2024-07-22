package com.woowacourse.friendogly.application.di

import android.content.Context
import com.woowacourse.friendogly.BuildConfig
import com.woowacourse.friendogly.data.repository.ClubRepositoryImpl
import com.woowacourse.friendogly.data.repository.KakaoLoginRepositoryImpl
import com.woowacourse.friendogly.data.repository.LocalRepositoryImpl
import com.woowacourse.friendogly.data.source.ClubDataSource
import com.woowacourse.friendogly.data.source.KakaoLoginDataSource
import com.woowacourse.friendogly.data.source.LocalDataSource
import com.woowacourse.friendogly.domain.repository.ClubRepository
import com.woowacourse.friendogly.domain.repository.KakaoLoginRepository
import com.woowacourse.friendogly.domain.repository.LocalRepository
import com.woowacourse.friendogly.domain.usecase.DeleteClubUseCase
import com.woowacourse.friendogly.domain.usecase.DeleteLocalDataUseCase
import com.woowacourse.friendogly.domain.usecase.GetClubMineUseCase
import com.woowacourse.friendogly.domain.usecase.GetJwtTokenUseCase
import com.woowacourse.friendogly.domain.usecase.KakaoLoginUseCase
import com.woowacourse.friendogly.domain.usecase.PostClubParticipationUseCase
import com.woowacourse.friendogly.domain.usecase.PostClubUseCase
import com.woowacourse.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.woowacourse.friendogly.kakao.source.KakaoLoginDataSourceImpl
import com.woowacourse.friendogly.local.di.LocalModule
import com.woowacourse.friendogly.local.source.LocalDataSourceImpl
import com.woowacourse.friendogly.remote.api.BaseUrl
import com.woowacourse.friendogly.remote.di.RemoteModule
import com.woowacourse.friendogly.remote.source.ClubDataSourceImpl

class AppModule(context: Context) {
    private val baseUrl = BaseUrl(BuildConfig.base_url)

    private val localModule = LocalModule(context)

    private val clubService =
        RemoteModule.createClubService(
            baseUrl = baseUrl,
            localModule = localModule,
        )

    // data source
    private val clubDataSource: ClubDataSource = ClubDataSourceImpl(service = clubService)
    private val localDataSource: LocalDataSource = LocalDataSourceImpl(localModule = localModule)
    private val kakaoLoginDataSource: KakaoLoginDataSource = KakaoLoginDataSourceImpl()

    // repository
    private val clubRepository: ClubRepository = ClubRepositoryImpl(source = clubDataSource)
    private val localRepository: LocalRepository = LocalRepositoryImpl(source = localDataSource)
    private val kakaoLoginRepository: KakaoLoginRepository =
        KakaoLoginRepositoryImpl(dataSource = kakaoLoginDataSource)

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

    companion object {
        private var instance: AppModule? = null

        fun setInstance(context: Context) {
            instance = AppModule(context)
        }

        fun getInstance(): AppModule = requireNotNull(instance)
    }
}
