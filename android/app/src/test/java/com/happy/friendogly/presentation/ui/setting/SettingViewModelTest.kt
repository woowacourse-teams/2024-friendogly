package com.happy.friendogly.presentation.ui.setting

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.usecase.DeleteMemberUseCase
import com.happy.friendogly.domain.usecase.DeleteTokenUseCase
import com.happy.friendogly.domain.usecase.GetChatAlarmUseCase
import com.happy.friendogly.domain.usecase.GetWoofAlarmUseCase
import com.happy.friendogly.domain.usecase.PostLogoutUseCase
import com.happy.friendogly.domain.usecase.SaveChatAlarmUseCase
import com.happy.friendogly.domain.usecase.SaveWoofAlarmUseCase
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(
    CoroutinesTestExtension::class,
    InstantTaskExecutorExtension::class,
    MockKExtension::class,
)
class SettingViewModelTest {
    private lateinit var viewModel: SettingViewModel

    @MockK
    private lateinit var deleteTokenUseCase: DeleteTokenUseCase

    @MockK
    private lateinit var getChatAlarmUseCase: GetChatAlarmUseCase

    @MockK
    private lateinit var getWoofAlarmUseCase: GetWoofAlarmUseCase

    @MockK
    private lateinit var saveChatAlarmUseCase: SaveChatAlarmUseCase

    @MockK
    private lateinit var saveWoofAlarmUseCase: SaveWoofAlarmUseCase

    @MockK
    private lateinit var deleteMemberUseCase: DeleteMemberUseCase

    @MockK
    private lateinit var postLogoutUseCase: PostLogoutUseCase

    @BeforeEach
    fun setUp() {
        coEvery { getChatAlarmUseCase() } returns Result.success(true)
        coEvery { getWoofAlarmUseCase() } returns Result.success(true)
        viewModel =
            SettingViewModel(
                deleteTokenUseCase,
                getChatAlarmUseCase,
                getWoofAlarmUseCase,
                saveChatAlarmUseCase,
                saveWoofAlarmUseCase,
                deleteMemberUseCase,
                postLogoutUseCase,
            )
    }

    @Test
    fun `초기화 시 알림 설정을 불러온다`() =
        runTest {
            // then
            val actual = viewModel.uiState.value ?: return@runTest
            Assertions.assertThat(actual.chattingAlarmPushPermitted).isEqualTo(true)
            Assertions.assertThat(actual.woofAlarmPushPermitted).isEqualTo(true)
        }

    @Test
    fun `알림 설정 저장 후 알림 상태가 업데이트된다`() =
        runTest {
            // given
            coEvery { saveChatAlarmUseCase(true) } returns Result.success(Unit)
            coEvery { saveWoofAlarmUseCase(true) } returns Result.success(Unit)

            // when
            viewModel.saveChattingAlarmSetting(true)
            viewModel.saveWoofAlarmSetting(true)

            // then
            coVerify { saveChatAlarmUseCase(true) }
            coVerify { saveWoofAlarmUseCase(true) }
        }

    @Test
    fun `로그아웃 시 토큰 삭제 후 회원가입 화면 이동 네비게이션을 호출한다`() =
        runTest {
            // given
            coEvery { postLogoutUseCase() } returns DomainResult.Success(Unit)
            coEvery { deleteTokenUseCase() } returns DomainResult.Success(Unit)

            // when
            viewModel.navigateToLogout()

            // then
            val actual = viewModel.navigateAction.value ?: return@runTest
            Assertions
                .assertThat(actual.value)
                .isEqualTo(SettingNavigationAction.NavigateToRegister)
        }

    @Test
    fun `회원 탈퇴 시 회원가입 화면 이동 네비게이션 동작을 호출한다`() =
        runTest {
            // given
            coEvery { deleteMemberUseCase() } returns DomainResult.Success(Unit)
            coEvery { deleteTokenUseCase() } returns DomainResult.Success(Unit)

            // when
            viewModel.navigateToUnsubscribe()

            // then
            val actual = viewModel.navigateAction.value ?: return@runTest
            Assertions
                .assertThat(actual.value)
                .isEqualTo(SettingNavigationAction.NavigateToRegister)
        }

    @Test
    fun `네트워크 오류 발생 시 네트워크 오류 메시지를 표시한다`() =
        runTest {
            // given
            coEvery { postLogoutUseCase() } returns DomainResult.Error(DataError.Network.NO_INTERNET)

            // when
            viewModel.navigateToLogout()

            // then
            val actual = viewModel.message.value ?: return@runTest
            Assertions
                .assertThat(actual.value)
                .isEqualTo(SettingMessage.NoInternetMessage)
        }
}
