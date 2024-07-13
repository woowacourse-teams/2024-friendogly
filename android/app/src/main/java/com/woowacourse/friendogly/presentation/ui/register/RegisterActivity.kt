package com.woowacourse.friendogly.presentation.ui.register

import androidx.activity.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityRegisterBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {
    private val viewModel: RegisterViewModel by viewModels()

    override fun initCreateView() {
        binding.vm = viewModel
    }
}
