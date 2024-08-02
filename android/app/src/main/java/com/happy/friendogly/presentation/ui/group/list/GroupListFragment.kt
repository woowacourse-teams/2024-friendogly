package com.happy.friendogly.presentation.ui.group.list

import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.FragmentGroupListBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.group.filter.bottom.GroupFilterBottomSheet
import com.happy.friendogly.presentation.ui.group.filter.bottom.ParticipationFilterBottomSheet
import com.happy.friendogly.presentation.ui.group.list.adapter.group.GroupListAdapter
import com.happy.friendogly.presentation.ui.group.list.adapter.selectfilter.SelectFilterAdapter

class GroupListFragment : BaseFragment<FragmentGroupListBinding>(R.layout.fragment_group_list) {
    private val viewModel: GroupListViewModel by viewModels<GroupListViewModel> {
        GroupListViewModel.factory(
            getAddressUseCase = AppModule.getInstance().getAddressUseCase,
        )
    }

    private val filterAdapter: SelectFilterAdapter by lazy {
        SelectFilterAdapter(viewModel as GroupListActionHandler)
    }
    private val groupAdapter: GroupListAdapter by lazy {
        GroupListAdapter(viewModel as GroupListActionHandler)
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadGroupsWithAddress()
    }

    override fun initViewCreated() {
        initDataBinding()
        initAdapter()
        initStateObserver()
        initObserver()
    }

    private fun initDataBinding() {
        binding.vm = viewModel

        with(binding.swipeRefreshLayoutGroupList) {
            setOnRefreshListener {
                viewModel.loadGroups()
                isRefreshing = false
            }
        }
    }

    private fun initAdapter() {
        binding.rcvGroupListFilter.adapter = filterAdapter
        binding.includeGroupList.rcvGroupListGroup.adapter = groupAdapter
    }

    private fun initObserver() {
        viewModel.groups.observe(viewLifecycleOwner) { groups ->
            groupAdapter.submitList(groups)
        }

        viewModel.groupFilterSelector.currentSelectedFilters.observe(viewLifecycleOwner) { filters ->
            filterAdapter.submitList(filters)
        }

        viewModel.groupListEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is GroupListEvent.OpenGroup ->
                    (activity as MainActivityActionHandler).navigateToGroupDetailActivity(event.groupId)

                GroupListEvent.Navigation.NavigateToAddGroup ->
                    (activity as MainActivityActionHandler).navigateToGroupAddActivity()

                is GroupListEvent.OpenParticipationFilter -> {
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

                is GroupListEvent.OpenFilterSelector -> {
                    val bottomSheet =
                        GroupFilterBottomSheet(
                            groupFilterType = event.groupFilterType,
                            currentFilters = event.groupFilters,
                        ) { filters ->
                            viewModel.updateGroupFilter(filters)
                        }
                    bottomSheet.show(parentFragmentManager, tag)
                    bottomSheet.setStyle(
                        DialogFragment.STYLE_NORMAL,
                        R.style.RoundCornerBottomSheetDialogTheme,
                    )
                }

                GroupListEvent.Navigation.NavigateToAddress ->
                    (activity as MainActivityActionHandler).navigateToSettingLocation()
            }
        }
    }

    private fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                GroupListUiState.Init -> {
                    binding.includeGroupList.rcvGroupListGroup.visibility = View.VISIBLE
                    binding.includeGroupData.linearLayoutGroupNotData.visibility = View.GONE
                    binding.includeGroupAddress.linearLayoutGroupNotAddress.visibility = View.GONE
                }

                GroupListUiState.NotAddress -> {
                    binding.includeGroupAddress.linearLayoutGroupNotAddress.visibility =
                        View.VISIBLE
                    binding.includeGroupList.rcvGroupListGroup.visibility = View.GONE
                    binding.includeGroupData.linearLayoutGroupNotData.visibility = View.GONE
                }

                GroupListUiState.NotData -> {
                    binding.includeGroupData.linearLayoutGroupNotData.visibility = View.VISIBLE
                    binding.includeGroupAddress.linearLayoutGroupNotAddress.visibility = View.GONE
                    binding.includeGroupList.rcvGroupListGroup.visibility = View.GONE
                }
            }
        }
    }
}
