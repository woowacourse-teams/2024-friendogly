package com.woowacourse.friendogly.presentation.ui.group.add.detal

import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentGroupAddDetailBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.group.add.GroupAddViewModel
import com.woowacourse.friendogly.presentation.ui.group.add.model.GroupCounter

class GroupAddDetailFragment :
    BaseFragment<FragmentGroupAddDetailBinding>(R.layout.fragment_group_add_detail) {
    private val viewModel: GroupAddViewModel by activityViewModels()

    override fun initViewCreated() {
        initDataBinding()
        initSeekBar()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initSeekBar() {
        binding.seekbarGroupCounter.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress >= GroupCounter.MIN_COUNT) {
                    viewModel.settingGroupCounter(progress)
                } else {
                    binding.seekbarGroupCounter.setProgress(GroupCounter.MIN_COUNT, true)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
