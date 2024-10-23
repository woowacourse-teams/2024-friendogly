package com.happy.friendogly.presentation.ui.registerpet

import androidx.lifecycle.SavedStateHandle
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.usecase.PatchPetUseCase
import com.happy.friendogly.domain.usecase.PostPetUseCase
import com.happy.friendogly.presentation.ui.registerpet.model.PetProfile
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(
    CoroutinesTestExtension::class,
    InstantTaskExecutorExtension::class,
    MockKExtension::class,
)
class RegisterPetViewModelTest {
    private lateinit var viewModel: RegisterPetViewModel

    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var postPetUseCase: PostPetUseCase

    @MockK
    private lateinit var patchPetUseCase: PatchPetUseCase

    @Test
    fun `강아지 정보를 정상적으로 등록할 수 있다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    set(RegisterPetActivity.PUT_EXTRA_PET_PROFILE, null)
                }
            coEvery {
                postPetUseCase(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Success(dummyPet)

            // when
            viewModel = RegisterPetViewModel(savedStateHandle, postPetUseCase, patchPetUseCase)
            viewModel.updatePetName(dummyPet.name)
            viewModel.updatePetDescription(dummyPet.description)
            viewModel.updatePetSize(PetSize.SMALL)
            viewModel.updatePetGender(PetGender.MAIL)
            viewModel.updatePetBirthday(2022, 1)
            viewModel.registerPet()

            // then
            val actual = viewModel.navigateAction.value ?: return@runTest
            Assertions
                .assertThat(actual.value)
                .isEqualTo(RegisterPetNavigationAction.NavigateToMyPage)
        }

    @Test
    fun `강아지 등록 시 네트워크 오류가 발생하면 적절한 메시지가 전송된다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    set(RegisterPetActivity.PUT_EXTRA_PET_PROFILE, null)
                }
            coEvery {
                postPetUseCase(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Error(DataError.Network.NO_INTERNET)

            // when
            viewModel = RegisterPetViewModel(savedStateHandle, postPetUseCase, patchPetUseCase)
            viewModel.updatePetName(dummyPet.name)
            viewModel.updatePetDescription(dummyPet.description)
            viewModel.updatePetSize(PetSize.SMALL)
            viewModel.updatePetGender(PetGender.MAIL)
            viewModel.updatePetBirthday(2022, 1)
            viewModel.registerPet()

            // then
            val message = viewModel.message.value ?: return@runTest
            Assertions
                .assertThat(message.value)
                .isInstanceOf(RegisterPetMessage.NoInternetMessage::class.java)
        }

    @Test
    fun `강아지 등록 시 이미지 크기가 5MB보다 크면 이미지 크기 오류 메시지가 전송된다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    set(RegisterPetActivity.PUT_EXTRA_PET_PROFILE, null)
                }
            coEvery {
                postPetUseCase(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Error(DataError.Network.FILE_SIZE_EXCEED)

            // when
            viewModel = RegisterPetViewModel(savedStateHandle, postPetUseCase, patchPetUseCase)
            viewModel.updatePetName(dummyPet.name)
            viewModel.updatePetDescription(dummyPet.description)
            viewModel.updatePetSize(PetSize.SMALL)
            viewModel.updatePetGender(PetGender.MAIL)
            viewModel.updatePetBirthday(2022, 1)
            viewModel.registerPet()

            // then
            val message = viewModel.message.value ?: return@runTest
            Assertions
                .assertThat(message.value)
                .isInstanceOf(RegisterPetMessage.FileSizeExceedMessage::class.java)
        }

    @Test
    fun `강아지 정보를 수정할 수 있다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    val value = Json.encodeToString(PetProfile.serializer(), dummyPetProfile)
                    set(RegisterPetActivity.PUT_EXTRA_PET_PROFILE, value)
                }
            coEvery {
                patchPetUseCase(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Success(Unit)

            // when
            viewModel = RegisterPetViewModel(savedStateHandle, postPetUseCase, patchPetUseCase)
            viewModel.updatePetName(dummyPet.name)
            viewModel.updatePetDescription(dummyPet.description)
            viewModel.updatePetSize(PetSize.SMALL)
            viewModel.updatePetGender(PetGender.MAIL)
            viewModel.updatePetBirthday(2023, 5)
            viewModel.registerPet()

            // then
            Assertions.assertThat(viewModel.uiState.value?.petId).isEqualTo(dummyPetProfile.id)
        }

    @Test
    fun `강아지 수정 시 네트워크 오류가 발생하면 적절한 메시지가 전송된다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    val value = Json.encodeToString(PetProfile.serializer(), dummyPetProfile)
                    set(RegisterPetActivity.PUT_EXTRA_PET_PROFILE, value)
                }
            coEvery {
                postPetUseCase(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Error(DataError.Network.NO_INTERNET)

            // when
            viewModel = RegisterPetViewModel(savedStateHandle, postPetUseCase, patchPetUseCase)
            viewModel.updatePetName(dummyPet.name)
            viewModel.updatePetDescription(dummyPet.description)
            viewModel.updatePetSize(PetSize.SMALL)
            viewModel.updatePetGender(PetGender.MAIL)
            viewModel.updatePetBirthday(2022, 1)
            viewModel.registerPet()

            // then
            val message = viewModel.message.value ?: return@runTest
            Assertions
                .assertThat(message.value)
                .isInstanceOf(RegisterPetMessage.NoInternetMessage::class.java)
        }

    @Test
    fun `강아지 수정 시 이미지 크기가 5MB보다 크면 이미지 크기 오류 메시지가 전송된다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    val value = Json.encodeToString(PetProfile.serializer(), dummyPetProfile)
                    set(RegisterPetActivity.PUT_EXTRA_PET_PROFILE, value)
                }
            coEvery {
                postPetUseCase(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } returns DomainResult.Error(DataError.Network.FILE_SIZE_EXCEED)

            // when
            viewModel = RegisterPetViewModel(savedStateHandle, postPetUseCase, patchPetUseCase)
            viewModel.updatePetName(dummyPet.name)
            viewModel.updatePetDescription(dummyPet.description)
            viewModel.updatePetSize(PetSize.SMALL)
            viewModel.updatePetGender(PetGender.MAIL)
            viewModel.updatePetBirthday(2022, 1)
            viewModel.registerPet()

            // then
            val message = viewModel.message.value ?: return@runTest
            Assertions
                .assertThat(message.value)
                .isInstanceOf(RegisterPetMessage.FileSizeExceedMessage::class.java)
        }

    companion object {
        private val dummyPetProfile =
            PetProfile(
                id = 1,
                name = "DummyPet",
                description = "Description of DummyPet",
                gender = Gender.MALE,
                sizeType = SizeType.SMALL,
                birthDate = LocalDate(2022, 1, 1),
                imageUrl = "dummy_url",
            )
        val dummyPet =
            Pet(
                id = 1L,
                memberId = 123L,
                name = "Buddy",
                description = "A friendly and playful dog.",
                birthDate = LocalDate(2020, 5, 15),
                sizeType = SizeType.MEDIUM,
                gender = Gender.MALE,
                imageUrl = "https://example.com/images/buddy.jpg",
            )
    }
}
