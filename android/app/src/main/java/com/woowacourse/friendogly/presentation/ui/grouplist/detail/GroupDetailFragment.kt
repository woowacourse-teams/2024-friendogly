package com.woowacourse.friendogly.presentation.ui.grouplist.detail

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentGroupDetailBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment

class GroupDetailFragment :
    BaseFragment<FragmentGroupDetailBinding>(R.layout.fragment_group_detail) {
    private val viewModel: GroupDetailViewModel by viewModels()
    override fun initViewCreated() {
        binding.vm = viewModel
    }
}
