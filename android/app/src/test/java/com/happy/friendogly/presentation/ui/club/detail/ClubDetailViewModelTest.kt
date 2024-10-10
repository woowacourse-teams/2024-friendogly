package com.happy.friendogly.presentation.ui.club.detail

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.domain.usecase.GetClubUseCase
import com.happy.friendogly.domain.usecase.PatchClubUseCase
import com.happy.friendogly.domain.usecase.PostClubMemberUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.ui.club.common.ClubErrorEvent
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import com.happy.friendogly.utils.TestFixture
import com.happy.friendogly.utils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
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
class ClubDetailViewModelTest {
    private lateinit var viewModel: ClubDetailViewModel

    @MockK
    private lateinit var analyticsHelper: AnalyticsHelper

    @MockK
    private lateinit var getClubUseCase: GetClubUseCase

    @MockK
    private lateinit var postClubMemberUseCase: PostClubMemberUseCase

    @MockK
    private lateinit var patchClubUseCase: PatchClubUseCase

    @BeforeEach
    fun setup() {
        analyticsHelper = mockk(relaxed = true)
        every { analyticsHelper.logEvent(any()) } returns Unit
    }

    @Test
    fun `모임 상세가 로드된다`() =
        runTest {
            // given
            val clubDetail =
                TestFixture.makeClubDetail(
                    clubState = ClubState.OPEN,
                )

            coEvery {
                getClubUseCase(0L)
            } returns DomainResult.Success(clubDetail)

            viewModel =
                ClubDetailViewModel(
                    analyticsHelper = analyticsHelper,
                    getClubUseCase = getClubUseCase,
                    postClubMemberUseCase = postClubMemberUseCase,
                    patchClubUseCase = patchClubUseCase,
                )

            // when
            viewModel.loadClub(0L)
            val actualClub = viewModel.club.getOrAwaitValue()

            // then
            Assertions.assertThat(actualClub.equals(clubDetail))
        }

    @Test
    fun `모임 상세 로드에 실패하면 사용자에게 실패를 알리는 이벤트를 발생시킨다`() =
        runTest {
            // given
            coEvery {
                getClubUseCase(0L)
            } returns DomainResult.Error(DataError.Network.SERVER_ERROR)

            viewModel =
                ClubDetailViewModel(
                    analyticsHelper = analyticsHelper,
                    getClubUseCase = getClubUseCase,
                    postClubMemberUseCase = postClubMemberUseCase,
                    patchClubUseCase = patchClubUseCase,
                )

            // when
            viewModel.loadClub(0L)
            val actualClubEvent = viewModel.clubErrorHandler.error.getOrAwaitValue()

            // then
            Assertions.assertThat(actualClubEvent.equals(ClubErrorEvent.ServerError))
        }

    @Test
    fun `모임 참여에 성공하면 사용자에게 참여 성공을 알리는 이벤트가 발생된다`() =
        runTest {
            // given
            val clubDetail =
                TestFixture.makeClubDetail(
                    clubState = ClubState.OPEN,
                    isMyPetsEmpty = true,
                )

            val clubParticipation = TestFixture.makeClubParticipation()

            coEvery {
                getClubUseCase(0L)
            } returns DomainResult.Success(clubDetail)

            coEvery {
                postClubMemberUseCase(
                    0L,
                    listOf(),
                )
            } returns DomainResult.Success(clubParticipation)

            viewModel =
                ClubDetailViewModel(
                    analyticsHelper = analyticsHelper,
                    getClubUseCase = getClubUseCase,
                    postClubMemberUseCase = postClubMemberUseCase,
                    patchClubUseCase = patchClubUseCase,
                )

            // when
            viewModel.loadClub(0L)
            viewModel.joinClub(listOf())
            val clubDetailEvent = viewModel.clubDetailEvent.getOrAwaitValue()

            // then
            Assertions.assertThat(clubDetailEvent.equals(ClubDetailEvent.Navigation.NavigateToChat(0L)))
        }

    @Test
    fun `모임 참여할 수 없는 경우 사용자에게 참여 실패를 알리는 이벤트가 발생한다`() =
        runTest {
            // given
            val clubDetail =
                TestFixture.makeClubDetail(
                    clubState = ClubState.OPEN,
                    isMyPetsEmpty = true,
                )

            coEvery {
                getClubUseCase(0L)
            } returns DomainResult.Success(clubDetail)

            coEvery {
                postClubMemberUseCase(
                    0L,
                    listOf(),
                )
            } returns DomainResult.Error(DataError.Network.SERVER_ERROR)

            viewModel =
                ClubDetailViewModel(
                    analyticsHelper = analyticsHelper,
                    getClubUseCase = getClubUseCase,
                    postClubMemberUseCase = postClubMemberUseCase,
                    patchClubUseCase = patchClubUseCase,
                )

            // when
            viewModel.loadClub(0L)
            viewModel.joinClub(listOf())
            val clubDetailEvent = viewModel.clubErrorHandler.error.getOrAwaitValue()

            // then
            Assertions.assertThat(clubDetailEvent.equals(ClubErrorEvent.ServerError))
        }
}
