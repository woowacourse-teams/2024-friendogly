package com.happy.friendogly.presentation.ui.club.add.detail

import android.widget.SeekBar
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
        binding.seekbarClubCounter.setOnSeekBarChangeListener(
            object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    if (progress >= ClubCounter.MIN_COUNT) {
                        viewModel.settingClubCounter(progress)
                    } else {
                        binding.seekbarClubCounter.setProgress(ClubCounter.MIN_COUNT, true)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            },
        )
    }
}
