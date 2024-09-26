package com.happy.friendogly.presentation.ui.club.add.information

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentClubAddInformationBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.ui.club.add.ClubAddViewModel
import com.happy.friendogly.presentation.utils.customOnFocusChangeListener
import com.happy.friendogly.presentation.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubAddInformationFragment :
    BaseFragment<FragmentClubAddInformationBinding>(R.layout.fragment_club_add_information) {
    private val viewModel: ClubAddViewModel by activityViewModels()

    override fun initViewCreated() {
        initDataBinding()
        initEditText()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditText() {
        binding.etClubContent.customOnFocusChangeListener(requireContext())
        binding.etClubSubject.customOnFocusChangeListener(requireContext())
        binding.llClubAddInformation.setOnTouchListener { _, _ ->
            hideKeyboard()
            binding.etClubSubject.clearFocus()
            binding.etClubContent.clearFocus()
            false
        }
    }
}
