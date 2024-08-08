package com.happy.friendogly.presentation.ui.club.list

import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.FragmentClubListBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.club.filter.bottom.ClubFilterBottomSheet
import com.happy.friendogly.presentation.ui.club.filter.bottom.ParticipationFilterBottomSheet
import com.happy.friendogly.presentation.ui.club.list.ClubListActionHandler
import com.happy.friendogly.presentation.ui.club.list.ClubListEvent
import com.happy.friendogly.presentation.ui.club.list.ClubListUiState
import com.happy.friendogly.presentation.ui.club.list.ClubListViewModel
import com.happy.friendogly.presentation.ui.club.list.adapter.club.ClubListAdapter
import com.happy.friendogly.presentation.ui.club.list.adapter.selectfilter.SelectFilterAdapter

class ClubListFragment : BaseFragment<FragmentClubListBinding>(R.layout.fragment_club_list) {
    private val viewModel: ClubListViewModel by viewModels<ClubListViewModel> {
        ClubListViewModel.factory(
            getAddressUseCase = AppModule.getInstance().getAddressUseCase,
            searchingClubsUseCase = AppModule.getInstance().getSearchingClubsUseCase,
        )
    }

    private val filterAdapter: SelectFilterAdapter by lazy {
        SelectFilterAdapter(viewModel as ClubListActionHandler)
    }
    private val clubAdapter: ClubListAdapter by lazy {
        ClubListAdapter(viewModel as ClubListActionHandler)
    }

    override fun initViewCreated() {
        initDataBinding()
        initAdapter()
        initStateObserver()
        initObserver()
    }

    private fun initDataBinding() {
        binding.vm = viewModel

        with(binding.swipeRefreshLayoutClubList) {
            setOnRefreshListener {
                viewModel.loadClubWithAddress()
                isRefreshing = false
            }
        }
    }

    private fun initAdapter() {
        binding.rcvClubListFilter.adapter = filterAdapter
        binding.includeClubList.rcvClubListClub.adapter = clubAdapter
    }

    private fun initObserver() {
        viewModel.clubs.observe(viewLifecycleOwner) { clubs ->
            clubAdapter.submitList(clubs)
        }

        viewModel.clubFilterSelector.currentSelectedFilters.observe(viewLifecycleOwner) { filters ->
            filterAdapter.submitList(filters)
        }

        viewModel.clubListEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is ClubListEvent.OpenClub ->
                    (activity as MainActivityActionHandler).navigateToClubDetailActivity(event.clubId)

                ClubListEvent.Navigation.NavigateToAddClub ->
                    (activity as MainActivityActionHandler).navigateToClubAddActivity()

                is ClubListEvent.OpenParticipationFilter -> {
                    val bottomSheet =
                        ParticipationFilterBottomSheet(
                            currentParticipationFilter = event.participationFilter,
                        ) { selectFilter ->
                            viewModel.updateParticipationFilter(selectFilter)
                        }
                    bottomSheet.show(parentFragmentManager, tag)
                    bottomSheet.setStyle(
                        DialogFragment.STYLE_NORMAL,
                        R.style.RoundCornerBottomSheetDialogTheme,
                    )
                }

                is ClubListEvent.OpenFilterSelector -> {
                    val bottomSheet =
                        ClubFilterBottomSheet(
                            clubFilterType = event.clubFilterType,
                            currentFilters = event.clubFilters,
                        ) { filters ->
                            viewModel.updateClubFilter(filters)
                        }
                    bottomSheet.show(parentFragmentManager, tag)
                    bottomSheet.setStyle(
                        DialogFragment.STYLE_NORMAL,
                        R.style.RoundCornerBottomSheetDialogTheme,
                    )
                }

                ClubListEvent.Navigation.NavigateToAddress ->
                    (activity as MainActivityActionHandler).navigateToSettingLocation()
            }
        }
    }

    private fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ClubListUiState.Init -> {
                    binding.includeClubList.rcvClubListClub.visibility = View.VISIBLE
                    binding.includeClubData.linearLayoutClubNotData.visibility = View.GONE
                    binding.includeClubAddress.linearLayoutClubNotAddress.visibility = View.GONE
                    binding.includeClubError.linearLayoutClubError.visibility = View.GONE
                }

                ClubListUiState.NotAddress -> {
                    binding.includeClubAddress.linearLayoutClubNotAddress.visibility =
                        View.VISIBLE
                    binding.includeClubList.rcvClubListClub.visibility = View.GONE
                    binding.includeClubData.linearLayoutClubNotData.visibility = View.GONE
                    binding.includeClubError.linearLayoutClubError.visibility = View.GONE
                }

                ClubListUiState.NotData -> {
                    binding.includeClubData.linearLayoutClubNotData.visibility = View.VISIBLE
                    binding.includeClubAddress.linearLayoutClubNotAddress.visibility = View.GONE
                    binding.includeClubList.rcvClubListClub.visibility = View.GONE
                    binding.includeClubError.linearLayoutClubError.visibility = View.GONE
                }

                ClubListUiState.Error -> {
                    binding.includeClubError.linearLayoutClubError.visibility = View.VISIBLE
                    binding.includeClubData.linearLayoutClubNotData.visibility = View.GONE
                    binding.includeClubAddress.linearLayoutClubNotAddress.visibility = View.GONE
                    binding.includeClubList.rcvClubListClub.visibility = View.GONE
                }
            }
        }
    }
}
