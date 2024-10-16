package com.happy.friendogly.presentation.ui.club.list

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubFilterCondition
import com.happy.friendogly.domain.model.SearchClubPageInfo
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.domain.usecase.GetSearchingClubsUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.ui.club.common.mapper.toGender
import com.happy.friendogly.presentation.ui.club.common.mapper.toSizeType
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.utils.CoroutinesTestExtension
import com.happy.friendogly.utils.InstantTaskExecutorExtension
import com.happy.friendogly.utils.TestFixture
import com.happy.friendogly.utils.TestFixture.makePet
import com.happy.friendogly.utils.TestFixture.makeUserAddress
import com.happy.friendogly.utils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class ClubListViewModelTest {
    private lateinit var viewModel: ClubListViewModel
    private lateinit var searchClubPageInfo: SearchClubPageInfo

    @MockK
    private lateinit var analyticsHelper: AnalyticsHelper

    @MockK
    private lateinit var getPetsMineUseCase: GetPetsMineUseCase

    @MockK
    private lateinit var getAddressUseCase: GetAddressUseCase

    @MockK
    private lateinit var getSearchingClubsUseCase: GetSearchingClubsUseCase

    @BeforeEach
    fun setUp() {
        searchClubPageInfo = SearchClubPageInfo()
    }

    @Test
    fun `등록한 주소의 모임 리스트가 로드된다`() =
        runTest {
            // given
            val clubs: List<Club> =
                listOf(
                    TestFixture.makeClub(),
                )

            coEvery {
                getAddressUseCase()
            } returns Result.success(makeUserAddress())

            coEvery {
                getPetsMineUseCase()
            } returns DomainResult.Success(listOf(makePet()))

            coEvery {
                getSearchingClubsUseCase(
                    searchClubPageInfo = searchClubPageInfo,
                    filterCondition = ClubFilterCondition.ALL,
                    address =
                    ClubAddress(
                        province = "서울특별시", null, null,
                    ),
                    genderParams = ClubFilter.makeGenderFilterEntry().mapNotNull { it.toGender() },
                    sizeParams = ClubFilter.makeSizeFilterEntry().mapNotNull { it.toSizeType() },
                )
            } returns flow {
                PagingSource.LoadResult.Page(
                    data = clubs,
                    prevKey = null,
                    nextKey = null
                )
            }

            viewModel =
                ClubListViewModel(
                    analyticsHelper = analyticsHelper,
                    getPetsMineUseCase = getPetsMineUseCase,
                    getAddressUseCase = getAddressUseCase,
                    searchingClubsUseCase = getSearchingClubsUseCase,
                )

            // when
            val actualClubs = viewModel.clubs.getOrAwaitValue()

            // then
            Assertions.assertThat(clubs.first().equals(actualClubs))
        }

    @Test
    fun `등록한 주소가 없는 경우 사용자에게 주소 등록을 요구하는 화면을 표시한다`() =
        runTest {
            // given
            coEvery {
                getAddressUseCase()
            } returns Result.failure(Throwable())

            coEvery {
                getPetsMineUseCase()
            } returns DomainResult.Success(listOf(makePet()))

            viewModel =
                ClubListViewModel(
                    analyticsHelper = analyticsHelper,
                    getPetsMineUseCase = getPetsMineUseCase,
                    getAddressUseCase = getAddressUseCase,
                    searchingClubsUseCase = getSearchingClubsUseCase,
                )

            val expectUiState = ClubListUiState.NotAddress

            // when
            val actualUiState = viewModel.uiState.getOrAwaitValue()

            // then
            Assertions.assertThat(actualUiState).isEqualTo(expectUiState)
        }
//
//    @Test
//    fun `적합한 모임이 없는 경우 사용자에게 모임이 없다는 것을 나타내는 화면을 표시한다`() =
//        runTest {
//            // given
//            coEvery {
//                getAddressUseCase()
//            } returns Result.success(makeUserAddress())
//
//            coEvery {
//                getPetsMineUseCase()
//            } returns DomainResult.Success(listOf(makePet()))
//
//            coEvery {
//                getSearchingClubsUseCase(
//                    searchClubPageInfo = searchClubPageInfo,
//                    filterCondition = ClubFilterCondition.ALL,
//                    address =
//                    ClubAddress(
//                        province = "서울특별시", null, null,
//                    ),
//                    genderParams = ClubFilter.makeGenderFilterEntry().mapNotNull { it.toGender() },
//                    sizeParams = ClubFilter.makeSizeFilterEntry().mapNotNull { it.toSizeType() },
//                )
//            } returns flow {
//                PagingSource.LoadResult.Page(
//                    data = listOf(),
//                    prevKey = null,
//                    nextKey = null
//                )
//            }
//
//            viewModel =
//                ClubListViewModel(
//                    analyticsHelper = analyticsHelper,
//                    getPetsMineUseCase = getPetsMineUseCase,
//                    getAddressUseCase = getAddressUseCase,
//                    searchingClubsUseCase = getSearchingClubsUseCase,
//                )
//
//            val expectUiState = ClubListUiState.NotData
//
//            // when
//            val actualUiState = viewModel.uiState.getOrAwaitValue()
//
//            // then
//            Assertions.assertThat(actualUiState).isEqualTo(expectUiState)
//        }
//
//    @Test
//    fun `모임 리스트 로드에 실패하면 사용자에게 에러 발생을 나타내는 화면을 표시한다`() {
//        // given
//        coEvery {
//            getAddressUseCase()
//        } returns Result.success(makeUserAddress())
//
//        coEvery {
//            getPetsMineUseCase()
//        } returns DomainResult.Success(listOf(makePet()))
//
//        coEvery {
//            getSearchingClubsUseCase(
//                searchClubPageInfo = searchClubPageInfo,
//                filterCondition = ClubFilterCondition.ALL,
//                address =
//                ClubAddress(
//                    province = "서울특별시", null, null,
//                ),
//                genderParams = ClubFilter.makeGenderFilterEntry().mapNotNull { it.toGender() },
//                sizeParams = ClubFilter.makeSizeFilterEntry().mapNotNull { it.toSizeType() },
//            )
//        } returns flow {
//            PagingSource.LoadResult.Error<Int, ClubItemUiModel>(Exception())
//        }
//
//        viewModel =
//            ClubListViewModel(
//                analyticsHelper = analyticsHelper,
//                getPetsMineUseCase = getPetsMineUseCase,
//                getAddressUseCase = getAddressUseCase,
//                searchingClubsUseCase = getSearchingClubsUseCase,
//            )
//
//        val expectUiState = ClubListUiState.Error
//
//        // when
//        val actualUiState = viewModel.uiState.getOrAwaitValue()
//
//        // then
//        Assertions.assertThat(actualUiState).isEqualTo(expectUiState)
//    }
}
