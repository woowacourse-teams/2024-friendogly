package com.woowacourse.friendogly.presentation.ui.group.add

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityGroupAddBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.MainActivity
import com.woowacourse.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter
import com.woowacourse.friendogly.presentation.utils.hideKeyboard

class GroupAddActivity : BaseActivity<ActivityGroupAddBinding>(R.layout.activity_group_add) {
    private val viewModel: GroupAddViewModel by viewModels()

    override fun initCreateView() {
        initDataBinding()
        initViewPager()
        initObserver()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initDataBinding(){
        binding.vm = viewModel
        binding.actionHandler = viewModel
    }

    private fun initViewPager() {
        binding.vpGroupAdd.adapter = GroupAddAdapter(this@GroupAddActivity)
        binding.vpGroupAdd.isUserInputEnabled = false
    }

    private fun initObserver(){
        viewModel.groupAddEvent.observeEvent(this){ actionEvent ->
            when(actionEvent){
                GroupAddEvent.Navigation.NavigateToHome -> {
                    startActivity(MainActivity.getIntent(this))
                    finish()
                }
                GroupAddEvent.Navigation.NavigateToSelectDog -> TODO()
                GroupAddEvent.Navigation.NavigateToSelectGroupPoster -> TODO()
                is GroupAddEvent.ChangePage -> {
                    binding.vpGroupAdd.setCurrentItem(actionEvent.page,true)
                }
            }
        }

    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, GroupAddActivity::class.java)
        }
    }
}
