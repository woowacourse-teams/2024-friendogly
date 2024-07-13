package com.woowacourse.friendogly.presentation.ui.register

import androidx.activity.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityRegisterBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.MainActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {
    private val viewModel: RegisterViewModel by viewModels()

    override fun initCreateView() {
        binding.vm = viewModel
        initObserve()
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is RegisterNavigationAction.NavigateToKakaoLogin -> {
                    startActivity(MainActivity.getIntent(this))
                    finish()
                }

                is RegisterNavigationAction.NavigateToGoogleLogin -> {
                    startActivity(MainActivity.getIntent(this))
                    finish()
                }
            }
        }
    }
}
