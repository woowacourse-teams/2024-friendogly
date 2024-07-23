package com.woowacourse.friendogly.presentation.ui.group.add.information

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentGroupAddInformationBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.group.add.GroupAddViewModel
import com.woowacourse.friendogly.presentation.utils.customOnFocusChangeListener
import com.woowacourse.friendogly.presentation.utils.hideKeyboard

class GroupAddInformationFragment :
    BaseFragment<FragmentGroupAddInformationBinding>(R.layout.fragment_group_add_information) {
    private val viewModel: GroupAddViewModel by activityViewModels()

    override fun initViewCreated() {
        initDataBinding()
        initEditText()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditText() {
        binding.etGroupContent.customOnFocusChangeListener(requireContext())
        binding.etGroupSubject.customOnFocusChangeListener(requireContext())
        binding.llGroupAddInformation.setOnTouchListener { _, _ ->
            hideKeyboard()
            binding.etGroupSubject.clearFocus()
            binding.etGroupContent.clearFocus()
            false
        }
    }
}
