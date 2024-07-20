package com.woowacourse.friendogly.presentation.ui.group.add.filter

import androidx.fragment.app.activityViewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentGroupAddFilterBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.group.add.GroupAddViewModel

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
