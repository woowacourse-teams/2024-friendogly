package com.happy.friendogly.presentation.ui.group.list

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentGroupListBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.group.list.adapter.group.GroupListAdapter
import com.happy.friendogly.presentation.ui.group.list.adapter.selectfilter.SelectFilterAdapter

class GroupListFragment : BaseFragment<FragmentGroupListBinding>(R.layout.fragment_group_list) {
    private val viewModel: GroupListViewModel by viewModels()
    private val groupAdapter: GroupListAdapter by lazy {
        GroupListAdapter(viewModel as GroupListActionHandler)
    }
    private val filterAdapter: SelectFilterAdapter by lazy {
        SelectFilterAdapter(viewModel as GroupListActionHandler)
    }
    private val adapter: ConcatAdapter by lazy {
        ConcatAdapter(filterAdapter, groupAdapter)
    }

    override fun initViewCreated() {
        initDataBinding()
        initAdapter()
        initObserver()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initAdapter() {
        binding.includeGroupListFilter.rcvGroupListGroup.adapter = adapter
    }

    private fun initObserver() {
        viewModel.groups.observe(viewLifecycleOwner) { groups ->
            groupAdapter.submitList(groups)
        }

        // TODO: 선택 필터 구현
        viewModel.groupFilterSelector.currentSelectedFilters.observe(viewLifecycleOwner) { filters ->
        }

        viewModel.groupListEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is GroupListEvent.OpenGroup ->
                    (activity as MainActivityActionHandler).navigateToGroupDetailActivity(event.groupId)

                GroupListEvent.Navigation.NavigateToAddGroup ->
                    (activity as MainActivityActionHandler).navigateToGroupAddActivity()
            }
        }
    }
}