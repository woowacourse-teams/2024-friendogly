package com.woowacourse.friendogly.presentation.ui.profilesetting

import android.annotation.SuppressLint
import androidx.activity.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityProfileSettingBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.MainActivity
import com.woowacourse.friendogly.presentation.utils.customOnFocusChangeListener
import com.woowacourse.friendogly.presentation.utils.hideKeyboard

class ProfileSettingActivity :
    BaseActivity<ActivityProfileSettingBinding>(R.layout.activity_profile_setting) {
    private val viewModel: ProfileSettingViewModel by viewModels()

    override fun initCreateView() {
        initDataBinding()
        initObserve()
        initEditText()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is ProfileSettingNavigationAction.NavigateToSetProfileImage -> {}

                is ProfileSettingNavigationAction.NavigateToHome -> {
                    startActivity(MainActivity.getIntent(this))
                    finish()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditText() {
        binding.etUserName.customOnFocusChangeListener(this)
        binding.constraintLayoutProfileSetMain.setOnTouchListener { _, _ ->
            hideKeyboard()
            binding.etUserName.clearFocus()
            false
        }
    }
}
