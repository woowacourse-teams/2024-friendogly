package com.happy.friendogly.presentation.ui.register

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.model.KakaoAccessToken
import com.happy.friendogly.domain.model.Login
import com.happy.friendogly.domain.usecase.GetFCMTokenUseCase
import com.happy.friendogly.domain.usecase.GetJwtTokenUseCase
import com.happy.friendogly.domain.usecase.PostKakaoLoginUseCase
import com.happy.friendogly.domain.usecase.SaveAlamTokenUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import com.happy.friendogly.utils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(
    CoroutinesTestExtension::class,
    InstantTaskExecutorExtension::class,
    MockKExtension::class,
)
class RegisterViewModelTest {
    private lateinit var viewModel: RegisterViewModel

    @MockK
    private lateinit var analyticsHelper: AnalyticsHelper

    @MockK
    private lateinit var getJwtTokenUseCase: GetJwtTokenUseCase

    @MockK
    private lateinit var postKakaoLoginUseCase: PostKakaoLoginUseCase

    @MockK
    private lateinit var saveJwtTokenUseCase: SaveJwtTokenUseCase

    @MockK
    private lateinit var saveAlarmTokenUseCase: SaveAlamTokenUseCase

    @MockK
    private lateinit var getFCMTokenUseCase: GetFCMTokenUseCase

    @BeforeEach
    fun setUp() {
        analyticsHelper = mockk(relaxed = true)
        every { analyticsHelper.logEvent(any()) } returns Unit
    }

    @Test
    fun `로컬 스토리지에 유효한 액세스 토큰이 있을 때, 모임 목록 화면 이동 이벤트가 발생한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(JWT)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterNavigationAction.NavigateToAlreadyLogin)
        }

    @Test
    fun `새로운 유저일 때, 로컬 스토리지에 토큰이 없다는 메시지 이벤트가 발생한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Error(DataError.Local.TOKEN_NOT_STORED)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )

            val actual = viewModel.message.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterMessage.TokenNotStoredErrorMessage)
        }

    @Test
    fun `로컬 스토리지에서 액세스 토큰을 가져올 때, 알 수 없는 에러가 발생하면, 기본 에러 메시지 이벤트가 발생한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Error(DataError.Local.LOCAL_ERROR)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )

            val actual = viewModel.message.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterMessage.DefaultErrorMessage)
        }

    @Test
    fun `사용자 등록을 하지 않은 경우, 소셜 로그인을 성공했을 때, 프로필 설정 이동 이벤트가 발생한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(NULL_JWT)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )

            coEvery { postKakaoLoginUseCase(accessToken = "accessToken") } returns
                DomainResult.Success(LOGIN)

            viewModel.postKakaoLogin(
                KakaoAccessToken(
                    accessToken = "accessToken",
                    idToken = "idToken",
                ),
            )

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(
                RegisterNavigationAction.NavigateToProfileSetting(
                    idToken = "accessToken",
                ),
            )
        }

    @Test
    fun `사용자 등록을 하지 않은 경우, 인터넷 문제로 소셜 로그인을 실패했을 때, 인터넷 에러 메시지 이벤트가 발생한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(NULL_JWT)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )

            coEvery { postKakaoLoginUseCase(accessToken = "accessToken") } returns
                DomainResult.Error(DataError.Network.NO_INTERNET)

            viewModel.postKakaoLogin(KAKAO_ACCESS_TOKEN)

            val actual = viewModel.message.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterMessage.NoInternetMessage)
        }

    @Test
    fun `사용자 등록을 하지 않은 경우, 서버 문제로 소셜 로그인을 실패했을 때, 서버 에러 메시지 이벤트가 발생한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(NULL_JWT)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )

            coEvery { postKakaoLoginUseCase(accessToken = "accessToken") } returns
                DomainResult.Error(DataError.Network.SERVER_ERROR)

            viewModel.postKakaoLogin(KAKAO_ACCESS_TOKEN)

            val actual = viewModel.message.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterMessage.ServerErrorMessage)
        }

    @Test
    fun `사용자 등록이 이미 된 경우, 소셜 로그인을 성공했을 때, 수신된 JWT를 로컬 저장소에 저장한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(NULL_JWT)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )

            coEvery { postKakaoLoginUseCase(accessToken = "accessToken") } returns
                DomainResult.Success(LOGIN)
            coEvery { saveAlarmTokenUseCase(token = "token") } returns Result.success(Unit)
            coEvery { saveJwtTokenUseCase(jwtToken = JWT) } returns DomainResult.Success(Unit)
            coEvery { getFCMTokenUseCase() } returns DomainResult.Success("token")

            viewModel.postKakaoLogin(KAKAO_ACCESS_TOKEN)

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterNavigationAction.NavigateToProfileSetting("accessToken"))
        }

    @Test
    fun `카카오 로그인 실행 시, 카카오 로그인 이동 이벤트가 발생한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(NULL_JWT)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )
            viewModel.executeKakaoLogin()

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterNavigationAction.NavigateToKakaoLogin)
        }

    @Test
    fun `구글 로그인 실행 시, 구글 로그인 이동 이벤트가 발생한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(NULL_JWT)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )
            viewModel.executeGoogleLogin()

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterNavigationAction.NavigateToGoogleLogin)
        }

    @Test
    fun `구글 로그인을 성공했을 때, 프로필 설정 이벤트가 발생한다`() =
        runTest {
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(NULL_JWT)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                    getFCMTokenUseCase = getFCMTokenUseCase,
                )
            viewModel.handleGoogleLogin(idToken = "idToken")

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(
                RegisterNavigationAction.NavigateToProfileSetting(
                    idToken = "idToken",
                ),
            )
        }

    companion object {
        val JWT = JwtToken(accessToken = "accessToken", refreshToken = "refreshToken")
        val NULL_JWT = JwtToken(accessToken = null, refreshToken = null)

        val LOGIN =
            Login(
                isRegistered = false,
                tokens = JWT,
            )

        val KAKAO_ACCESS_TOKEN =
            KakaoAccessToken(
                accessToken = "accessToken",
                idToken = "idToken",
            )
    }
}
