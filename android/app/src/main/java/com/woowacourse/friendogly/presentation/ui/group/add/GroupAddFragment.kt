package com.woowacourse.friendogly.presentation.ui.group.add

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentGroupAddBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment

class GroupAddFragment : BaseFragment<FragmentGroupAddBinding>(R.layout.fragment_group_add) {
    private val viewModel: GroupAddViewModel by viewModels()

    override fun initViewCreated() {
        initDataBinding()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }
}
