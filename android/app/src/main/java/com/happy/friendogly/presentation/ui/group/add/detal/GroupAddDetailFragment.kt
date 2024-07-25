package com.happy.friendogly.presentation.ui.group.add.detal

import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentGroupAddDetailBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.ui.group.add.GroupAddViewModel
import com.happy.friendogly.presentation.ui.group.add.model.GroupCounter

class GroupAddDetailFragment :
    BaseFragment<FragmentGroupAddDetailBinding>(R.layout.fragment_group_add_detail) {
    private val viewModel: GroupAddViewModel by activityViewModels()

    override fun initViewCreated() {
        initDataBinding()
        initSeekBar()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
        binding.actionHandler = viewModel
    }

    private fun initSeekBar() {
        binding.seekbarGroupCounter.setOnSeekBarChangeListener(
            object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    if (progress >= GroupCounter.MIN_COUNT) {
                        viewModel.settingGroupCounter(progress)
                    } else {
                        binding.seekbarGroupCounter.setProgress(GroupCounter.MIN_COUNT, true)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            },
        )
    }
}
