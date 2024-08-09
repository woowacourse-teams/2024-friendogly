package com.happy.friendogly.presentation.ui.club.add.filter

import androidx.fragment.app.activityViewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentClubAddFilterBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.ui.club.add.ClubAddViewModel

class ClubAddFilterFragment :
    BaseFragment<FragmentClubAddFilterBinding>(R.layout.fragment_club_add_filter) {
    private val viewModel: ClubAddViewModel by activityViewModels()

    override fun initViewCreated() {
        initDataBinding()
        checkAllFilters()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun checkAllFilters() {
        binding.includeAddClubBig.checkboxClubFilter.isChecked = true
        binding.includeAddClubMedium.checkboxClubFilter.isChecked = true
        binding.includeAddClubSmail.checkboxClubFilter.isChecked = true
        binding.includeAddClubNeutralizingFemale.checkboxClubFilter.isChecked = true
        binding.includeAddClubNeutralizingMale.checkboxClubFilter.isChecked = true
        binding.includeAddClubMale.checkboxClubFilter.isChecked = true
        binding.includeAddClubFemale.checkboxClubFilter.isChecked = true
    }
}
