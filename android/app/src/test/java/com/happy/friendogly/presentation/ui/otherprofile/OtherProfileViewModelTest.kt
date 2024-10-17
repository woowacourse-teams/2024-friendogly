package com.happy.friendogly.presentation.ui.otherprofile

import androidx.lifecycle.SavedStateHandle
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.usecase.GetMemberUseCase
import com.happy.friendogly.domain.usecase.GetPetsUseCase
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(
    CoroutinesTestExtension::class,
    InstantTaskExecutorExtension::class,
    MockKExtension::class,
)
class OtherProfileViewModelTest {
    private lateinit var viewModel: OtherProfileViewModel

    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var getPetsUseCase: GetPetsUseCase

    @MockK
    private lateinit var getMemberUseCase: GetMemberUseCase

    @Test
    fun `다른 사용자의 정보와 사용자의 강아지 정보를 불러올 수 있다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    set(OtherProfileActivity.PUT_EXTRA_USER_ID, OTHER_PROFILE_ID)
                }
            coEvery { getMemberUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyMember)
            coEvery { getPetsUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyPets)

            // when
            viewModel =
                OtherProfileViewModel(
                    savedStateHandle,
                    getPetsUseCase,
                    getMemberUseCase,
                )

            // then
            val actual = viewModel.uiState.value ?: return@runTest
            Assertions.assertThat(actual.nickname).isEqualTo(dummyMember.name)
            Assertions.assertThat(actual.tag).isEqualTo(dummyMember.tag)
            Assertions.assertThat(actual.profilePath).isEqualTo(dummyMember.imageUrl)
            Assertions.assertThat(actual.pets).isEqualTo(dummyPets)
        }

    @Test
    fun `다른 사용자의 정보를 불러올 때, 인터넷이 끊긴다면 인터넷 연결이 없다는 메시지를 보여줄 수 있다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    set(OtherProfileActivity.PUT_EXTRA_USER_ID, OTHER_PROFILE_ID)
                }
            coEvery { getMemberUseCase(OTHER_PROFILE_ID) } returns DomainResult.Error(DataError.Network.NO_INTERNET)
            coEvery { getPetsUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyPets)

            // when
            viewModel =
                OtherProfileViewModel(
                    savedStateHandle,
                    getPetsUseCase,
                    getMemberUseCase,
                )

            // then
            val actual = viewModel.message.value ?: return@runTest
            Assertions.assertThat(actual.value).isEqualTo(OtherProfileMessage.NoInternetMessage)
        }

    @Test
    fun `강아지 정보를 불러올 때, 서버 오류가 발생하면 서버 오류 메시지를 보여줄 수 있다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    set(OtherProfileActivity.PUT_EXTRA_USER_ID, OTHER_PROFILE_ID)
                }
            coEvery { getMemberUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyMember)
            coEvery { getPetsUseCase(OTHER_PROFILE_ID) } returns DomainResult.Error(DataError.Network.SERVER_ERROR)

            // when
            viewModel =
                OtherProfileViewModel(
                    savedStateHandle,
                    getPetsUseCase,
                    getMemberUseCase,
                )

            // then
            val actual = viewModel.message.value ?: return@runTest
            Assertions.assertThat(actual.value).isEqualTo(OtherProfileMessage.ServerErrorMessage)
        }

    @Test
    fun `현재 페이지를 업데이트 할 수 있다`() =
        runTest {
            // given
            val page = 2
            savedStateHandle =
                SavedStateHandle().apply {
                    set(OtherProfileActivity.PUT_EXTRA_USER_ID, OTHER_PROFILE_ID)
                }
            coEvery { getMemberUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyMember)
            coEvery { getPetsUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyPets)
            viewModel =
                OtherProfileViewModel(
                    savedStateHandle,
                    getPetsUseCase,
                    getMemberUseCase,
                )

            // when
            viewModel.updateCurrentPage(page)

            // then
            val actualPage = viewModel.currentPage.value
            Assertions.assertThat(actualPage).isEqualTo(page)
        }

    @Test
    fun `뒤로 가기 네비게이션을 수행할 수 있다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    set(OtherProfileActivity.PUT_EXTRA_USER_ID, OTHER_PROFILE_ID)
                }
            coEvery { getMemberUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyMember)
            coEvery { getPetsUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyPets)
            viewModel =
                OtherProfileViewModel(
                    savedStateHandle,
                    getPetsUseCase,
                    getMemberUseCase,
                )

            // when
            viewModel.navigateToBack()

            // then
            val actualAction =
                viewModel.navigateAction.value ?: return@runTest
            Assertions
                .assertThat(actualAction.value)
                .isEqualTo(OtherProfileNavigationAction.NavigateToBack)
        }

    @Test
    fun `특정 강아지의 세부 정보 화면으로 이동할 수 있다`() =
        runTest {
            // given
            savedStateHandle =
                SavedStateHandle().apply {
                    set(OtherProfileActivity.PUT_EXTRA_USER_ID, OTHER_PROFILE_ID)
                }
            coEvery { getMemberUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyMember)
            coEvery { getPetsUseCase(OTHER_PROFILE_ID) } returns DomainResult.Success(dummyPets)

            // when
            viewModel =
                OtherProfileViewModel(
                    savedStateHandle,
                    getPetsUseCase,
                    getMemberUseCase,
                )
            viewModel.navigateToPetDetail()

            // then
            val actualAction =
                viewModel.navigateAction.value ?: return@runTest
            Assertions
                .assertThat(actualAction.value)
                .isInstanceOf(OtherProfileNavigationAction.NavigateToPetDetail::class.java)
        }

    companion object {
        const val OTHER_PROFILE_ID = 0L
        val dummyMember =
            Member(
                id = OTHER_PROFILE_ID,
                name = "에디",
                tag = "#1234",
                imageUrl = "",
            )

        val dummyPets =
            listOf(
                Pet(
                    id = 1L,
                    memberId = 101L,
                    name = "Buddy",
                    description = "A friendly and playful dog.",
                    birthDate = LocalDate(2020, 5, 10),
                    sizeType = SizeType.SMALL,
                    gender = Gender.MALE,
                    imageUrl = "https://example.com/images/buddy.jpg",
                ),
                Pet(
                    id = 2L,
                    memberId = 102L,
                    name = "Mittens",
                    description = "A curious cat who loves to explore.",
                    birthDate = LocalDate(2018, 3, 22),
                    sizeType = SizeType.MEDIUM,
                    gender = Gender.FEMALE,
                    imageUrl = "https://example.com/images/mittens.jpg",
                ),
                Pet(
                    id = 3L,
                    memberId = 103L,
                    name = "Charlie",
                    description = "A gentle giant with a calm demeanor.",
                    birthDate = LocalDate(2019, 8, 15),
                    sizeType = SizeType.LARGE,
                    gender = Gender.MALE,
                    imageUrl = "https://example.com/images/char",
                ),
            )
    }
}
