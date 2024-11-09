package com.happy.friendogly.presentation.ui.club.my.head

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.usecase.GetMyHeadClubUseCase
import com.happy.friendogly.presentation.ui.club.common.mapper.toPresentation
import com.happy.friendogly.presentation.ui.club.my.MyClubUiState
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import com.happy.friendogly.utils.TestFixture
import com.happy.friendogly.utils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(
    CoroutinesTestExtension::class,
    InstantTaskExecutorExtension::class,
    MockKExtension::class,
)
class MyHeadClubViewModelTest {
    private lateinit var viewModel: MyHeadClubViewModel

    @MockK
    private lateinit var getMyHeadClubUseCase: GetMyHeadClubUseCase

    @Test
    fun `내가 생성한 모임이 로드된다`() =
        runTest {
            // given
            val clubs: List<Club> =
                listOf(
                    TestFixture.makeClub(),
                )

            coEvery {
                getMyHeadClubUseCase()
            } returns DomainResult.Success(clubs)

            viewModel =
                MyHeadClubViewModel(
                    getMyHeadClubUseCase = getMyHeadClubUseCase,
                )

            // when
            val actualClubs = viewModel.myHeadClubs.getOrAwaitValue()

            // then
            Assertions.assertEquals(clubs.toPresentation(), actualClubs)
        }

    @Test
    fun `내가 생성한 모임이 없다면, 모임이 없다는 것을 나타내는 상태를 가진다`() =
        runTest {
            // given
            val clubs: List<Club> =
                listOf()

            coEvery {
                getMyHeadClubUseCase()
            } returns DomainResult.Success(clubs)

            viewModel =
                MyHeadClubViewModel(
                    getMyHeadClubUseCase = getMyHeadClubUseCase,
                )
            val expectUiState = MyClubUiState.NotData

            // when
            val actualUiState = viewModel.myHeadClubUiState.getOrAwaitValue()

            // then
            Assertions.assertEquals(expectUiState, actualUiState)
        }

    @Test
    fun `내가 생성한 모임 로드에 실패한다면, 에러를 표시하는 상태를 가진다`() =
        runTest {
            // given
            coEvery {
                getMyHeadClubUseCase()
            } returns DomainResult.Error(DataError.Network.SERVER_ERROR)

            viewModel =
                MyHeadClubViewModel(
                    getMyHeadClubUseCase = getMyHeadClubUseCase,
                )
            val expectUiState = MyClubUiState.Error

            // when
            val actualUiState = viewModel.myHeadClubUiState.getOrAwaitValue()

            // then
            Assertions.assertEquals(expectUiState, actualUiState)
        }
}
