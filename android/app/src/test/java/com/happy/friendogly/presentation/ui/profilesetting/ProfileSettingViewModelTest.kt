package com.happy.friendogly.presentation.ui.profilesetting

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.model.Register
import com.happy.friendogly.domain.usecase.PatchMemberUseCase
import com.happy.friendogly.domain.usecase.PostMemberUseCase
import com.happy.friendogly.domain.usecase.SaveAlamTokenUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile
import com.happy.friendogly.presentation.utils.toMultipartBody
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import com.happy.friendogly.utils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File

@ExperimentalCoroutinesApi
@ExtendWith(
    CoroutinesTestExtension::class,
    InstantTaskExecutorExtension::class,
    MockKExtension::class,
)
class ProfileSettingViewModelTest {
    private lateinit var viewModel: ProfileSettingViewModel

    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var saveAlarmTokenUseCase: SaveAlamTokenUseCase

    @MockK
    private lateinit var postMemberUseCase: PostMemberUseCase

    @MockK
    private lateinit var saveJwtTokenUseCase: SaveJwtTokenUseCase

    @MockK
    private lateinit var patchMemberUseCase: PatchMemberUseCase

    @Test
    fun `프로필 이미지 이동 이벤트가 발생한다`() {
        savedStateHandle =
            SavedStateHandle().apply {
                set(ProfileSettingActivity.PUT_EXTRA_PROFILE, null)
                set(ProfileSettingActivity.PUT_EXTRA_ACCESS_TOKEN, "accessToken")
            }
        viewModel =
            ProfileSettingViewModel(
                saveAlarmTokenUseCase,
                savedStateHandle,
                postMemberUseCase,
                saveJwtTokenUseCase,
                patchMemberUseCase,
            )
        viewModel.selectProfileImage()

        val actual = viewModel.navigateAction.getOrAwaitValue()
        Assertions.assertThat(actual.value)
            .isEqualTo(ProfileSettingNavigationAction.NavigateToSetProfileImage)
    }

    @Test
    fun `프로필 이미지를 변경할 수 있다`() {
        savedStateHandle =
            SavedStateHandle().apply {
                set(ProfileSettingActivity.PUT_EXTRA_PROFILE, null)
                set(ProfileSettingActivity.PUT_EXTRA_ACCESS_TOKEN, "accessToken")
            }
        viewModel =
            ProfileSettingViewModel(
                saveAlarmTokenUseCase,
                savedStateHandle,
                postMemberUseCase,
                saveJwtTokenUseCase,
                patchMemberUseCase,
            )

        val bitmap = mockk<Bitmap>(relaxed = true)
        viewModel.updateProfileImage(bitmap)
        val file = File("/tmp/image.jpg")
        val partBody = file.toMultipartBody()
        viewModel.updateProfileFile(partBody)

        val actual = viewModel.uiState

        Assertions.assertThat(actual.value?.profileImage)
            .isEqualTo(bitmap)

        Assertions.assertThat(actual.value?.profilePath)
            .isEqualTo(partBody)
    }

    @Test
    fun `프로필 이미지를 기본 이미지로 변경할 수 있다`() {
        savedStateHandle =
            SavedStateHandle().apply {
                set(ProfileSettingActivity.PUT_EXTRA_PROFILE, null)
                set(ProfileSettingActivity.PUT_EXTRA_ACCESS_TOKEN, "accessToken")
            }
        viewModel =
            ProfileSettingViewModel(
                saveAlarmTokenUseCase,
                savedStateHandle,
                postMemberUseCase,
                saveJwtTokenUseCase,
                patchMemberUseCase,
            )

        viewModel.resetProfileImage()

        val actual = viewModel.uiState

        Assertions.assertThat(actual.value?.profileImage)
            .isEqualTo(null)

        Assertions.assertThat(actual.value?.profilePath)
            .isEqualTo(null)
    }

//    @Test
//    fun `프로필을 등록 할 수 있다`() =
//        runTest {
//            savedStateHandle =
//                SavedStateHandle().apply {
//                    set(ProfileSettingActivity.PUT_EXTRA_PROFILE, null)
//                    set(ProfileSettingActivity.PUT_EXTRA_ACCESS_TOKEN, "accessToken")
//                }
//            viewModel =
//                ProfileSettingViewModel(
//                    saveAlarmTokenUseCase,
//                    savedStateHandle,
//                    postMemberUseCase,
//                    saveJwtTokenUseCase,
//                    patchMemberUseCase,
//                )
//
//            coEvery {
//                postMemberUseCase(
//                    any(),
//                    any(),
//                    any(),
//                )
//            } returns DomainResult.Success(REGISTER)
//            coEvery { saveJwtTokenUseCase(jwtToken = JWT_TOKEN) } returns DomainResult.Success(Unit)
//
//            viewModel.isButtonActive.value = true
//            viewModel.nickname.value = "에디"
//            viewModel.submitProfileSelection()
//
//            val actual = viewModel.navigateAction.getOrAwaitValue()
//            Assertions.assertThat(actual.value)
//                .isEqualTo(ProfileSettingNavigationAction.NavigateToHome)
//        }

    @Test
    fun `프로필을 편집 할 수 있다`() =
        runTest {
            savedStateHandle =
                SavedStateHandle().apply {
                    val value = Json.encodeToString(Profile.serializer(), PROFILE)
                    set(
                        ProfileSettingActivity.PUT_EXTRA_PROFILE,
                        value,
                    )
                    set(ProfileSettingActivity.PUT_EXTRA_ACCESS_TOKEN, "accessToken")
                }
            viewModel =
                ProfileSettingViewModel(
                    saveAlarmTokenUseCase,
                    savedStateHandle,
                    postMemberUseCase,
                    saveJwtTokenUseCase,
                    patchMemberUseCase,
                )

            coEvery {
                patchMemberUseCase(
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Success(MEMBER)
            coEvery { saveJwtTokenUseCase(jwtToken = JWT_TOKEN) } returns DomainResult.Success(Unit)

            viewModel.isButtonActive.value = true
            viewModel.nickname.value = "에디"
            viewModel.submitProfileSelection()

            val actual = viewModel.navigateAction.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(ProfileSettingNavigationAction.NavigateToMyPage)
        }

    @Test
    fun `프로필을 편집 할 때, 이미지 크기가 1MB보다 크다면, 이미지 크기가 1MB보다 크다는 메시지 이벤트가 발생한다`() =
        runTest {
            savedStateHandle =
                SavedStateHandle().apply {
                    val value = Json.encodeToString(Profile.serializer(), PROFILE)
                    set(
                        ProfileSettingActivity.PUT_EXTRA_PROFILE,
                        value,
                    )
                    set(ProfileSettingActivity.PUT_EXTRA_ACCESS_TOKEN, "accessToken")
                }
            viewModel =
                ProfileSettingViewModel(
                    saveAlarmTokenUseCase,
                    savedStateHandle,
                    postMemberUseCase,
                    saveJwtTokenUseCase,
                    patchMemberUseCase,
                )

            coEvery {
                patchMemberUseCase(
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Error(DataError.Network.FILE_SIZE_EXCEED)

            viewModel.isButtonActive.value = true
            viewModel.nickname.value = "에디"
            viewModel.submitProfileSelection()

            val actual = viewModel.message.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(ProfileSettingMessage.FileSizeExceedMessage)
        }

    @Test
    fun `프로필을 편집 할 때, 인터넷 문제가 발생한다면, 인터넷 에러 메시지 이벤트가 발생한다`() =
        runTest {
            savedStateHandle =
                SavedStateHandle().apply {
                    val value = Json.encodeToString(Profile.serializer(), PROFILE)
                    set(
                        ProfileSettingActivity.PUT_EXTRA_PROFILE,
                        value,
                    )
                    set(ProfileSettingActivity.PUT_EXTRA_ACCESS_TOKEN, "accessToken")
                }
            viewModel =
                ProfileSettingViewModel(
                    saveAlarmTokenUseCase,
                    savedStateHandle,
                    postMemberUseCase,
                    saveJwtTokenUseCase,
                    patchMemberUseCase,
                )

            coEvery {
                patchMemberUseCase(
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Error(DataError.Network.NO_INTERNET)

            viewModel.isButtonActive.value = true
            viewModel.nickname.value = "에디"
            viewModel.submitProfileSelection()

            val actual = viewModel.message.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(ProfileSettingMessage.NoInternetMessage)
        }

    @Test
    fun `프로필을 편집 할 때, 서버 에러가 발생한다면, 서버 에러 메시지 이벤트가 발생한다`() =
        runTest {
            savedStateHandle =
                SavedStateHandle().apply {
                    val value = Json.encodeToString(Profile.serializer(), PROFILE)
                    set(
                        ProfileSettingActivity.PUT_EXTRA_PROFILE,
                        value,
                    )
                    set(ProfileSettingActivity.PUT_EXTRA_ACCESS_TOKEN, "accessToken")
                }
            viewModel =
                ProfileSettingViewModel(
                    saveAlarmTokenUseCase,
                    savedStateHandle,
                    postMemberUseCase,
                    saveJwtTokenUseCase,
                    patchMemberUseCase,
                )

            coEvery {
                patchMemberUseCase(
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Error(DataError.Network.SERVER_ERROR)

            viewModel.isButtonActive.value = true
            viewModel.nickname.value = "에디"
            viewModel.submitProfileSelection()

            val actual = viewModel.message.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(ProfileSettingMessage.ServerErrorMessage)
        }

    companion object {
        val PROFILE =
            Profile(
                name = "name",
                imageUrl = "imageUrl",
            )

        val JWT_TOKEN =
            JwtToken(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
            )

        val REGISTER =
            Register(
                id = 1L,
                name = "에디",
                tag = "tag",
                imageUrl = "imageUrl",
                tokens = JWT_TOKEN,
            )

        val MEMBER =
            Member(
                id = 1L,
                name = "에디",
                tag = "tag",
                imageUrl = "imageUrl",
            )
    }
}
