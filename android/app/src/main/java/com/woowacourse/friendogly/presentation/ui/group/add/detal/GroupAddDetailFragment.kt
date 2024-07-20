package com.woowacourse.friendogly.presentation.ui.group.add.detal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentGroupAddDetailBinding
import com.woowacourse.friendogly.databinding.FragmentGroupDetailBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.group.add.GroupAddViewModel


class GroupAddDetailFragment : BaseFragment<FragmentGroupAddDetailBinding>(R.layout.fragment_group_add_detail) {
    private val viewModel : GroupAddViewModel by activityViewModels()

    override fun initViewCreated() {
        initDataBinding()
    }

    private fun initDataBinding(){
        binding.vm = viewModel
    }
}
