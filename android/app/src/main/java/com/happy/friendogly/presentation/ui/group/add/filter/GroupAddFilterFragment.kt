package com.happy.friendogly.presentation.ui.group.add.filter

import androidx.fragment.app.activityViewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentGroupAddFilterBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.ui.group.add.GroupAddViewModel

class GroupAddFilterFragment :
    BaseFragment<FragmentGroupAddFilterBinding>(R.layout.fragment_group_add_filter) {
    private val viewModel: GroupAddViewModel by activityViewModels()

    override fun initViewCreated() {
        initDataBinding()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }
}
