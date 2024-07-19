package com.woowacourse.friendogly.presentation.ui.register

import androidx.activity.viewModels
import com.google.android.gms.common.api.ApiException
import com.woowacourse.friendogly.App.Companion.kakaoModule
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityRegisterBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.profilesetting.ProfileSettingActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {
    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModel.factory(kakaoLoginUseCase = kakaoModule.kakaoLoginUseCase)
    }

    private val googleSignInLauncher =
        registerForActivityResult(GoogleSignInContract()) { task ->
            val account =
                task?.getResult(ApiException::class.java) ?: return@registerForActivityResult
            val idToken = account.idToken ?: return@registerForActivityResult
            viewModel.handleGoogleLogin(idToken = idToken)
        }

    override fun initCreateView() {
        initDataBinding()
        initObserve()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is RegisterNavigationAction.NavigateToGoogleLogin ->
                    googleSignInLauncher.launch(
                        SIGN_IN_REQUEST_CODE,
                    )

                is RegisterNavigationAction.NavigateToProfileSetting ->
                    startActivity(
                        ProfileSettingActivity.getIntent(this),
                    )
            }
        }
    }

    companion object {
        const val SIGN_IN_REQUEST_CODE = 1
    }
}
