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
        checkAllFilters()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun checkAllFilters(){
        binding.includeAddGroupBig.checkboxGroupFilter.isChecked = true
        binding.includeAddGroupMedium.checkboxGroupFilter.isChecked = true
        binding.includeAddGroupSmail.checkboxGroupFilter.isChecked = true
        binding.includeAddGroupNeutralizingFemale.checkboxGroupFilter.isChecked = true
        binding.includeAddGroupNeutralizingMale.checkboxGroupFilter.isChecked = true
        binding.includeAddGroupMale.checkboxGroupFilter.isChecked = true
        binding.includeAddGroupFemale.checkboxGroupFilter.isChecked = true
    }
}
