package com.woowacourse.friendogly.presentation.ui.group.add.information

import androidx.fragment.app.activityViewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentGroupAddInformationBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.group.add.GroupAddViewModel
import com.woowacourse.friendogly.presentation.utils.customOnFocusChangeListener

class GroupAddInformationFragment : BaseFragment<FragmentGroupAddInformationBinding>(R.layout.fragment_group_add_information) {
    private val viewModel: GroupAddViewModel by activityViewModels()

    override fun initViewCreated() {
        initDataBinding()
        initObserve()
        initEditText()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initObserve() {
    }

    private fun initEditText() {
        binding.etGroupContent.customOnFocusChangeListener(requireContext())
        binding.etGroupSubject.customOnFocusChangeListener(requireContext())
    }
}
