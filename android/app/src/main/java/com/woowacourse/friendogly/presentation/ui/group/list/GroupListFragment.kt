package com.woowacourse.friendogly.presentation.ui.group.list

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentGroupListBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.group.list.adapter.group.GroupListAdapter

class GroupListFragment : BaseFragment<FragmentGroupListBinding>(R.layout.fragment_group_list) {
    private val viewModel: GroupListViewModel by viewModels()
    private val adapter: GroupListAdapter by lazy {
        GroupListAdapter(viewModel as GroupListActionHandler)
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
        binding.rcvGroupList.adapter = adapter
    }

    private fun initObserver() {
        viewModel.groups.observe(viewLifecycleOwner) { groups ->
            adapter.submitList(groups)
        }

        viewModel.groupFilterSelector.currentSelectedFilters.observe(viewLifecycleOwner) { filters ->

        }
    }
}
