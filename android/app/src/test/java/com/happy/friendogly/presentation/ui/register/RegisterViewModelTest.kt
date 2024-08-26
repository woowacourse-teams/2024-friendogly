package com.happy.friendogly.presentation.ui.register

import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.model.KakaoAccessToken
import com.happy.friendogly.domain.model.Login
import com.happy.friendogly.domain.usecase.GetJwtTokenUseCase
import com.happy.friendogly.domain.usecase.PostKakaoLoginUseCase
import com.happy.friendogly.domain.usecase.SaveAlamTokenUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
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

    @BeforeEach
    fun setUp() {
        analyticsHelper = mockk(relaxed = true)
        every { analyticsHelper.logEvent(any()) } returns Unit
    }

    @Test
    fun `로컬 스토리지에 유효한 액세스 토큰이 있을 때, 모임 목록 화면 이동 이벤트가 발생한다`() =
        runTest {
            val jwtToken = JwtToken(accessToken = "accessToken", refreshToken = "refreshToken")
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(jwtToken)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                )

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterNavigationAction.NavigateToAlreadyLogin)
        }

    @Test
    fun `사용자 등록을 하지 않은 경우, 소셜 로그인을 성공했을 때, 프로필 설정 이동 이벤트가 발생한다`() =
        runTest {
            val jwtToken = JwtToken(accessToken = null, refreshToken = null)
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(jwtToken)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                )

            val login =
                Login(
                    isRegistered = false,
                    tokens = JwtToken(accessToken = "accessToken", refreshToken = "refreshToken"),
                )
            coEvery { postKakaoLoginUseCase(accessToken = "accessToken") } returns
                DomainResult.Success(login)

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

    // TODO Friebase Token 저장 방식을 Coroutines로 변경 시 사용
//    @Test
//    fun `사용자 등록이 이미 된 경우, 소셜 로그인을 성공했을 때, 수신된 JWT를 로컬 저장소에 저장한다`() =
//        runTest {
//            val jwtToken = JwtToken(accessToken = null, refreshToken = null)
//            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(jwtToken)
//            viewModel =
//                RegisterViewModel(
//                    analyticsHelper = analyticsHelper,
//                    getJwtTokenUseCase = getJwtTokenUseCase,
//                    postKakaoLoginUseCase = postKakaoLoginUseCase,
//                    saveJwtTokenUseCase = saveJwtTokenUseCase,
//                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
//                )
//
//            val login =
//                Login(
//                    isRegistered = true,
//                    tokens = JwtToken(accessToken = "accessToken", refreshToken = "refreshToken"),
//                )
//            coEvery { postKakaoLoginUseCase(accessToken = "accessToken") } returns
//                DomainResult.Success(login)
//
//            val tokens = login.tokens ?: return@runTest
//
//            coEvery { saveAlarmTokenUseCase(token = "token") } returns Result.success(Unit)
//            coEvery { saveJwtTokenUseCase(jwtToken = tokens) } returns DomainResult.Success(Unit)
//
//            viewModel.postKakaoLogin(
//                KakaoAccessToken(
//                    accessToken = "accessToken",
//                    idToken = "idToken",
//                ),
//            )
//
//            val actual = viewModel.navigateAction.getOrAwaitValue()
//            assertThat(actual.value).isEqualTo(RegisterNavigationAction.NavigateToAlreadyLogin)
//        }

    @Test
    fun `카카오 로그인 실행 시, 카카오 로그인 이동 이벤트가 발생한다`() =
        runTest {
            val jwtToken = JwtToken(accessToken = null, refreshToken = null)
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(jwtToken)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                )
            viewModel.executeKakaoLogin()

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterNavigationAction.NavigateToKakaoLogin)
        }

    @Test
    fun `구글 로그인 실행 시, 구글 로그인 이동 이벤트가 발생한다`() =
        runTest {
            val jwtToken = JwtToken(accessToken = null, refreshToken = null)
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(jwtToken)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                )
            viewModel.executeGoogleLogin()

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(RegisterNavigationAction.NavigateToGoogleLogin)
        }

    @Test
    fun `구글 로그인을 성공했을 때, 프로필 설정 이벤트가 발생한다`() =
        runTest {
            val jwtToken = JwtToken(accessToken = null, refreshToken = null)
            coEvery { getJwtTokenUseCase() } returns DomainResult.Success(jwtToken)
            viewModel =
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                )
            viewModel.handleGoogleLogin(idToken = "idToken")

            val actual = viewModel.navigateAction.getOrAwaitValue()
            assertThat(actual.value).isEqualTo(
                RegisterNavigationAction.NavigateToProfileSetting(
                    idToken = "idToken",
                ),
            )
        }
}
