package com.happy.friendogly.presentation.ui.club.list

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentClubListBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.PetAddAlertDialog
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.club.common.ClubChangeStateIntent
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.MessageHandler
import com.happy.friendogly.presentation.ui.club.common.handleError
import com.happy.friendogly.presentation.ui.club.filter.bottom.ClubFilterBottomSheet
import com.happy.friendogly.presentation.ui.club.filter.bottom.ParticipationFilterBottomSheet
import com.happy.friendogly.presentation.ui.club.list.adapter.club.ClubListAdapter
import com.happy.friendogly.presentation.ui.club.list.adapter.selectfilter.SelectFilterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClubListFragment : BaseFragment<FragmentClubListBinding>(R.layout.fragment_club_list) {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val viewModel: ClubListViewModel by viewModels()

    private val filterAdapter: SelectFilterAdapter by lazy {
        SelectFilterAdapter(viewModel as ClubListActionHandler)
    }
    private lateinit var clubAdapter: ClubListAdapter

    override fun initViewCreated() {
        initDataBinding()
        initAdapter()
        initStateObserver()
        initCollect()
        initObserver()
        initClubListResultLauncher()
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
        setClubAdapter()
        binding.rcvClubListFilter.adapter = filterAdapter
    }

    private fun initClubListResultLauncher() {
        resultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val isChange =
                        result.data?.getBooleanExtra(
                            ClubChangeStateIntent.CHANGE_CLUB_STATE,
                            false,
                        ) ?: false
                    if (isChange) {
                        viewModel.loadClubWithAddress()
                    }
                }
            }
    }

    private fun initCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.clubs.collectLatest {
                    clubAdapter.submitData(it)
                }
            }
        }
    }

    private fun handleLoadStateErrors(vararg states: LoadState) {
        states.forEach { state ->
            if (state is LoadState.Error) {
                viewModel.handleDomainError(state.error)
            }
        }
    }

    private fun handleLoadStateLoadings(vararg states: LoadState) {
        states.forEach { state ->
            if (state.endOfPaginationReached) {
                viewModel.updateInitState()
            }
        }
    }

    private fun setClubAdapter() {
        clubAdapter = ClubListAdapter(viewModel as ClubItemActionHandler)
        binding.includeClubList.rcvClubListClub.adapter = clubAdapter
        clubAdapter.addLoadStateListener { loadState ->
            if (loadState.hasError) {
                handleLoadStateErrors(loadState.append, loadState.prepend, loadState.refresh)
            } else {
                handleLoadStateLoadings(loadState.append, loadState.prepend, loadState.refresh)
            }
        }
    }

    private fun initObserver() {
        viewModel.clubFilterSelector.currentSelectedFilters.observe(viewLifecycleOwner) { filters ->
            filterAdapter.submitList(filters)
        }

        viewModel.clubListEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is ClubListEvent.OpenClub ->
                    (activity as MainActivityActionHandler).navigateToClubDetailActivity(
                        clubId = event.clubId,
                        resultLauncher = resultLauncher,
                    )

                ClubListEvent.Navigation.NavigateToAddClub ->
                    (activity as MainActivityActionHandler).navigateToClubAddActivity(
                        resultLauncher = resultLauncher,
                    )

                is ClubListEvent.OpenParticipationFilter -> {
                    val bottomSheet =
                        ParticipationFilterBottomSheet(
                            currentParticipationFilter = event.participationFilter,
                        ) { selectFilter ->
                            viewModel.updateParticipationFilter(selectFilter)
                        }
                    bottomSheet.show(parentFragmentManager, tag)
                    bottomSheet.setStyle(
                        BottomSheetDialogFragment.STYLE_NORMAL,
                        R.style.RoundTopCornerBottomSheetDialogTheme,
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
                        BottomSheetDialogFragment.STYLE_NORMAL,
                        R.style.RoundTopCornerBottomSheetDialogTheme,
                    )
                }

                ClubListEvent.Navigation.NavigateToAddress ->
                    (activity as MainActivityActionHandler).navigateToSettingLocation(resultLauncher)

                ClubListEvent.FailLocation -> showSnackbar(getString(R.string.club_add_information_fail_address))
                ClubListEvent.OpenAddPet -> openRegisterPetDialog()
                ClubListEvent.ResetPaging -> {
                    setClubAdapter()
                    viewModel.loadClubs()
                }
            }
        }

        viewModel.clubErrorHandler.error.observeEvent(viewLifecycleOwner) {
            it.handleError(
                sendMessage = { message ->
                    when (message) {
                        is MessageHandler.SendSnackBar -> showSnackbar(getString(message.messageId))
                        is MessageHandler.SendToast -> showToastMessage(getString(message.messageId))
                    }
                },
            )
        }
    }

    private fun openRegisterPetDialog() {
        PetAddAlertDialog(
            clickToNegative = {},
            clickToPositive = {
                (activity as MainActivityActionHandler).navigateToRegisterPet(null)
            },
        ).show(parentFragmentManager, tag)
    }

    private fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ClubListUiState.Init -> applyViewVisibility(binding.includeClubList.rcvClubListClub)

                ClubListUiState.NotAddress -> applyViewVisibility(binding.includeClubAddress.linearLayoutClubNotAddress)

                ClubListUiState.NotData -> applyViewVisibility(binding.includeClubData.linearLayoutClubNotData)

                ClubListUiState.Error -> applyViewVisibility(binding.includeClubError.linearLayoutClubError)

                ClubListUiState.Loading -> applyViewVisibility(binding.includeClubLoading.nestedViewLayoutClubLoading)
            }
        }
    }

    private fun applyViewVisibility(currentView: View) {
        binding.includeClubLoading.nestedViewLayoutClubLoading.visibility = View.GONE
        binding.includeClubError.linearLayoutClubError.visibility = View.GONE
        binding.includeClubData.linearLayoutClubNotData.visibility = View.GONE
        binding.includeClubAddress.linearLayoutClubNotAddress.visibility = View.GONE
        binding.includeClubList.rcvClubListClub.visibility = View.GONE
        currentView.visibility = View.VISIBLE
    }

    companion object {
        const val TAG = "ClubListFragment"
    }
}
