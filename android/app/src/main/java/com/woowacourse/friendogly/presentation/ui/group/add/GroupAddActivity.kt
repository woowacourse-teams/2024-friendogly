package com.woowacourse.friendogly.presentation.ui.group.add

import androidx.activity.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityGroupAddBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter

class GroupAddActivity : BaseActivity<ActivityGroupAddBinding>(R.layout.activity_group_add) {
    private val viewModel: GroupAddViewModel by viewModels()

    override fun initCreateView() {
        initViewPager()
        initObserver()
    }

    private fun initViewPager() {
        binding.vpGroupAdd.adapter = GroupAddAdapter(this@GroupAddActivity)
        binding.vpGroupAdd.isUserInputEnabled = false
    }

    private fun initObserver(){
    }
}
