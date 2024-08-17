package com.happy.friendogly.presentation.ui.club.add.detail

import androidx.fragment.app.activityViewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentClubAddDetailBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.ui.club.add.ClubAddViewModel
import com.happy.friendogly.presentation.ui.club.add.model.ClubCounter

class ClubAddDetailFragment :
    BaseFragment<FragmentClubAddDetailBinding>(R.layout.fragment_club_add_detail) {
    private val viewModel: ClubAddViewModel by activityViewModels()

    override fun initViewCreated() {
        initDataBinding()
        initSeekBar()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
        binding.actionHandler = viewModel
    }

    private fun initSeekBar() {
        binding.numberPickerClubAddCounter.apply {
            minValue = ClubCounter.MIN_COUNT
            maxValue = ClubCounter.MAX_COUNT
            setOnValueChangedListener { _, _, newVal ->
                if (newVal >= ClubCounter.MIN_COUNT && newVal <= ClubCounter.MAX_COUNT) {
                    viewModel.settingClubCounter(newVal)
                }
            }
        }
    }
}
