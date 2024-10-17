package com.happy.friendogly.presentation.ui.recentpet

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.RecentPet
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.usecase.GetAllRecentPetUseCase
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import com.happy.friendogly.utils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@ExtendWith(
    CoroutinesTestExtension::class,
    InstantTaskExecutorExtension::class,
    MockKExtension::class,
)
class RecentPetViewModelTest {
    private lateinit var viewModel: RecentPetViewModel

    @MockK
    private lateinit var getAllRecentPetUseCase: GetAllRecentPetUseCase

    @Test
    fun `최근 펫 정보가 로드된다`() =
        runTest {
            // given
            coEvery { getAllRecentPetUseCase() } returns DomainResult.Success(dummyRecentPets)

            // when
            viewModel = RecentPetViewModel(getAllRecentPetUseCase)

            // Then
            val actualUiState = viewModel.uiState.getOrAwaitValue()
            val expectedUiState =
                listOf(
                    RecentPetDateView.from(0, LocalDate(2023, 10, 1)),
                    RecentPetView.from(1, dummyRecentPets[0], false),
                    RecentPetDateView.from(2, LocalDate(2023, 10, 2)),
                    RecentPetView.from(3, dummyRecentPets[1], true),
                )
            assertThat(actualUiState.recentPets).isEqualTo(expectedUiState)
        }

    @Test
    fun `펫이 없는 경우 UI 상태가 빈 리스트로 업데이트된다`() =
        runTest {
            // given
            val emptyPets: List<RecentPet> = emptyList()
            coEvery { getAllRecentPetUseCase() } returns DomainResult.Success(emptyPets)

            // when
            viewModel = RecentPetViewModel(getAllRecentPetUseCase)

            // Then
            val actualUiState = viewModel.uiState.getOrAwaitValue()
            assertThat(actualUiState.recentPets).isEmpty()
        }

    companion object {
        val dummyRecentPets =
            listOf(
                RecentPet(
                    id = 1L,
                    memberId = 101L,
                    name = "Buddy",
                    birthday = LocalDate(2020, 6, 12),
                    imgUrl = "https://example.com/buddy.jpg",
                    gender = Gender.MALE,
                    sizeType = SizeType.MEDIUM,
                    createAt = LocalDateTime.of(2023, 10, 1, 10, 30),
                ),
                RecentPet(
                    id = 2L,
                    memberId = 102L,
                    name = "Max",
                    birthday = LocalDate(2019, 4, 5),
                    imgUrl = "https://example.com/max.jpg",
                    gender = Gender.MALE,
                    sizeType = SizeType.LARGE,
                    createAt = LocalDateTime.of(2023, 10, 2, 14, 15),
                ),
            )
    }
}
