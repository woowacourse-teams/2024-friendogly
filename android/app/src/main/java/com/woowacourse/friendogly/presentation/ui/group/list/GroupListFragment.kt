package com.woowacourse.friendogly.presentation.ui.group.list

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentGroupListBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment

class GroupListFragment() : BaseFragment<FragmentGroupListBinding>(R.layout.fragment_group_list) {
    private val viewModel: GroupListViewModel by viewModels()

    override fun initViewCreated() {
        binding.vm = viewModel
    }
}
