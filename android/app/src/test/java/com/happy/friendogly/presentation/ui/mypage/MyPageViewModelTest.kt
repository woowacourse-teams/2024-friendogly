package com.happy.friendogly.presentation.ui.mypage

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.usecase.GetMemberMineUseCase
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.presentation.ui.petdetail.PetDetail
import com.happy.friendogly.presentation.ui.petdetail.PetsDetail
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile
import com.happy.friendogly.presentation.ui.registerpet.model.PetProfile
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import com.happy.friendogly.utils.getOrAwaitValue
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
class MyPageViewModelTest {
    private lateinit var viewModel: MyPageViewModel

    @MockK
    private lateinit var getPetsMineUseCase: GetPetsMineUseCase

    @MockK
    private lateinit var getMemberMineUseCase: GetMemberMineUseCase

    @Test
    fun `사용자와 펫 정보가 로드된다`() =
        runTest {
            val pets: List<Pet> = listOf(PET, PET.copy(id = 2L), PET.copy(id = 3L))
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            val actual = viewModel.uiState.getOrAwaitValue()
            Assertions.assertThat(actual.id).isEqualTo(MEMBER.id)
            Assertions.assertThat(actual.tag).isEqualTo(MEMBER.tag)
            Assertions.assertThat(actual.imageUrl).isEqualTo(MEMBER.imageUrl)
            Assertions.assertThat(actual.nickname).isEqualTo(MEMBER.name)
            Assertions.assertThat(actual.pets)
                .isEqualTo(pets.map { pet -> PetView.from(pet = pet) } + PetAddView(memberId = MEMBER.id))
        }

    @Test
    fun `사용자의 펫이 5마리일 경우, 펫 더하기 아이템은 추가되지 않는다`() =
        runTest {
            val pets: List<Pet> =
                listOf(
                    PET,
                    PET.copy(id = 2L),
                    PET.copy(id = 3L),
                    PET.copy(id = 4L),
                    PET.copy(id = 5L),
                )
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            val actual = viewModel.uiState.getOrAwaitValue()
            Assertions.assertThat(actual.id).isEqualTo(MEMBER.id)
            Assertions.assertThat(actual.tag).isEqualTo(MEMBER.tag)
            Assertions.assertThat(actual.imageUrl).isEqualTo(MEMBER.imageUrl)
            Assertions.assertThat(actual.nickname).isEqualTo(MEMBER.name)
            Assertions.assertThat(actual.pets)
                .isEqualTo(pets.map { pet -> PetView.from(pet = pet) })
        }

    @Test
    fun `사용자의 정보를 로드할 때, 인터넷 에러가 발생하면, 인터넷 에러 메시지 이벤트가 발생한다`() =
        runTest {
            val pets: List<Pet> =
                listOf(
                    PET,
                    PET.copy(id = 2L),
                    PET.copy(id = 3L),
                    PET.copy(id = 4L),
                    PET.copy(id = 5L),
                )
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Error(DataError.Network.NO_INTERNET)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            val actual = viewModel.message.getOrAwaitValue()
            Assertions.assertThat(actual.value).isEqualTo(MyPageMessage.NoInternetMessage)
        }

    @Test
    fun `사용자의 정보를 로드할 때, 서버 에러가 발생하면, 서버 에러 메시지 이벤트가 발생한다`() =
        runTest {
            val pets: List<Pet> =
                listOf(
                    PET,
                    PET.copy(id = 2L),
                    PET.copy(id = 3L),
                    PET.copy(id = 4L),
                    PET.copy(id = 5L),
                )
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Error(DataError.Network.SERVER_ERROR)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            val actual = viewModel.message.getOrAwaitValue()
            Assertions.assertThat(actual.value).isEqualTo(MyPageMessage.ServerErrorMessage)
        }

    @Test
    fun `사용자의 펫 정보를 로드할 때, 인터넷 에러가 발생하면, 인터넷 에러 메시지 이벤트가 발생한다`() =
        runTest {
            coEvery { getPetsMineUseCase() } returns DomainResult.Error(DataError.Network.NO_INTERNET)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)
            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            val actual = viewModel.message.getOrAwaitValue()
            Assertions.assertThat(actual.value).isEqualTo(MyPageMessage.NoInternetMessage)
        }

    @Test
    fun `사용자의 펫 정보를 로드할 때, 서버 에러가 발생하면, 서버 에러 메시지 이벤트가 발생한다`() =
        runTest {
            coEvery { getPetsMineUseCase() } returns DomainResult.Error(DataError.Network.SERVER_ERROR)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)
            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            val actual = viewModel.message.getOrAwaitValue()
            Assertions.assertThat(actual.value).isEqualTo(MyPageMessage.ServerErrorMessage)
        }

    @Test
    fun `펫 정보 페이지를 5페이지로 변경할 수 있다`() =
        runTest {
            val pets: List<Pet> = listOf(PET, PET.copy(id = 2L), PET.copy(id = 3L))
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            viewModel.updateCurrentPage(5)

            val actual = viewModel.currentPage.getOrAwaitValue()
            Assertions.assertThat(actual).isEqualTo(5)
        }

    @Test
    fun `펫 상세 정보 페이지 이동 이벤트가 발생한다`() =
        runTest {
            val pets: List<Pet> = listOf(PET, PET.copy(id = 2L), PET.copy(id = 3L))
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            viewModel.navigateToPetDetail()
            val petDetail =
                pets.map { pet ->
                    PetDetail(
                        id = pet.id,
                        name = pet.name,
                        description = pet.description,
                        birthDate = pet.birthDate,
                        sizeType = pet.sizeType,
                        gender = pet.gender,
                        imageUrl = pet.imageUrl,
                    )
                }

            val petsDetail = PetsDetail(petDetail)

            val actual = viewModel.navigateAction.getOrAwaitValue()
            Assertions.assertThat(actual.value).isEqualTo(
                MyPageNavigationAction.NavigateToPetDetail(
                    currentPage = 0,
                    petsDetail = petsDetail,
                ),
            )
        }

    @Test
    fun `펫 등록 이동 이벤트가 발생한다`() =
        runTest {
            val pets: List<Pet> = listOf(PET, PET.copy(id = 2L), PET.copy(id = 3L))
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            viewModel.navigateToRegisterDog(id = MEMBER.id)
            val actual = viewModel.navigateAction.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(MyPageNavigationAction.NavigateToDogRegister)
        }

    @Test
    fun `사용자 프로필 편집 이동 이벤트가 발생한다`() =
        runTest {
            val pets: List<Pet> = listOf(PET, PET.copy(id = 2L), PET.copy(id = 3L))
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            viewModel.navigateToProfileEdit()
            val profile =
                Profile(
                    name = MEMBER.name,
                    imageUrl = MEMBER.imageUrl,
                )

            val actual = viewModel.navigateAction.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(MyPageNavigationAction.NavigateToProfileEdit(profile))
        }

    @Test
    fun `설정 이동 이벤트가 발생한다`() =
        runTest {
            val pets: List<Pet> = listOf(PET, PET.copy(id = 2L), PET.copy(id = 3L))
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            viewModel.navigateToSetting()

            val actual = viewModel.navigateAction.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(MyPageNavigationAction.NavigateToSetting)
        }

    @Test
    fun `펫 편집 이동 이벤트가 발생한다`() =
        runTest {
            val pets: List<Pet> = listOf(PET, PET.copy(id = 2L), PET.copy(id = 3L))
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            viewModel.navigateToPetEdit()

            val actual = viewModel.navigateAction.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(MyPageNavigationAction.NavigateToPetEdit(PET_PROFILE))
        }

    @Test
    fun `내가 참여한 모임 이동 이벤트가 발생한다`() =
        runTest {
            val pets: List<Pet> = listOf(PET, PET.copy(id = 2L), PET.copy(id = 3L))
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            viewModel.navigateToMyParticipation()

            val actual = viewModel.navigateAction.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(MyPageNavigationAction.NavigateToMyParticipation)
        }

    @Test
    fun `내가 방장인 모임 이동 이벤트가 발생한다`() =
        runTest {
            val pets: List<Pet> = listOf(PET, PET.copy(id = 2L), PET.copy(id = 3L))
            coEvery { getPetsMineUseCase() } returns DomainResult.Success(pets)
            coEvery { getMemberMineUseCase() } returns DomainResult.Success(MEMBER)

            viewModel =
                MyPageViewModel(
                    getPetsMineUseCase = getPetsMineUseCase,
                    getMemberMineUseCase = getMemberMineUseCase,
                )

            viewModel.navigateToMyClubManger()

            val actual = viewModel.navigateAction.getOrAwaitValue()
            Assertions.assertThat(actual.value)
                .isEqualTo(MyPageNavigationAction.NavigateToMyClubManger)
        }

    companion object {
        val MEMBER =
            Member(
                id = 1L,
                name = "name",
                tag = "tag",
                imageUrl = "imageUrl",
            )

        val PET =
            Pet(
                id = 1L,
                memberId = 1L,
                name = "name",
                description = "description",
                birthDate = LocalDate.parse("2024-08-29"),
                sizeType = SizeType.SMALL,
                gender = Gender.MALE,
                imageUrl = "imageUrl",
            )

        val PET_PROFILE =
            PetProfile(
                id = PET.id,
                name = PET.name,
                description = PET.description,
                birthDate = PET.birthDate,
                sizeType = PET.sizeType,
                gender = PET.gender,
                imageUrl = PET.imageUrl,
            )
    }
}
