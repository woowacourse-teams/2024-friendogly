package com.happy.friendogly.presentation.ui.club.list

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentClubListBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.PetAddAlertDialog
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.club.common.ClubChangeStateIntent
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.adapter.club.ClubListAdapter
import com.happy.friendogly.presentation.ui.club.common.observeClubError
import com.happy.friendogly.presentation.ui.club.filter.bottom.ClubFilterBottomSheet
import com.happy.friendogly.presentation.ui.club.filter.bottom.ParticipationFilterBottomSheet
import com.happy.friendogly.presentation.ui.club.list.adapter.selectfilter.SelectFilterAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubListFragment : BaseFragment<FragmentClubListBinding>(R.layout.fragment_club_list) {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val viewModel: ClubListViewModel by viewModels()

    private val filterAdapter: SelectFilterAdapter by lazy {
        SelectFilterAdapter(viewModel as ClubListActionHandler)
    }
    private val clubAdapter: ClubListAdapter by lazy {
        ClubListAdapter(viewModel as ClubItemActionHandler)
    }

    override fun initViewCreated() {
        initDataBinding()
        initAdapter()
        initStateObserver()
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
        binding.rcvClubListFilter.adapter = filterAdapter
        binding.includeClubList.rcvClubListClub.adapter = clubAdapter
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
                    (activity as MainActivityActionHandler).navigateToSettingLocation(resultLauncher)

                ClubListEvent.FailLocation -> showSnackbar(getString(R.string.club_add_information_fail_address))
                ClubListEvent.OpenAddPet -> openRegisterPetDialog()
            }
        }

        viewModel.clubErrorHandler.observeClubError(
            owner = viewLifecycleOwner,
            sendSnackBar = { messageId -> showSnackbar(getString(messageId)) },

            sendToast = { messageId -> showToastMessage(getString(messageId)) },
        )
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
            }
        }
    }

    private fun applyViewVisibility(currentView: View) {
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
