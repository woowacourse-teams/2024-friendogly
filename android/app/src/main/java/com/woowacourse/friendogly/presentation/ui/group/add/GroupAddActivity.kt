package com.woowacourse.friendogly.presentation.ui.group.add

import androidx.activity.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityGroupAddBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.MainActivity
import com.woowacourse.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter

class GroupAddActivity : BaseActivity<ActivityGroupAddBinding>(R.layout.activity_group_add) {
    private val viewModel: GroupAddViewModel by viewModels()

    override fun initCreateView() {
        initDataBinding()
        initViewPager()
        initObserver()
    }

    private fun initDataBinding(){
        binding.vm = viewModel
        binding.actionHandler = viewModel
    }

    private fun initViewPager() {
        binding.vpGroupAdd.adapter = GroupAddAdapter(this@GroupAddActivity)
        binding.vpGroupAdd.isUserInputEnabled = false
    }

    private fun initObserver(){
        viewModel.navigationAction.observeEvent(this){ actionEvent ->
            when(actionEvent){
                GroupAddNavigationAction.NavigateToHome -> {
                    startActivity(MainActivity.getIntent(this))
                    finish()
                }
                GroupAddNavigationAction.NavigateToSelectGroupPoster -> {

                }
                GroupAddNavigationAction.NavigateToSelectDog -> {

                }
            }
        }
    }
}
