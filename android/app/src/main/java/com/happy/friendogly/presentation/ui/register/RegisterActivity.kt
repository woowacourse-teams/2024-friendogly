package com.happy.friendogly.presentation.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.common.api.ApiException
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivityRegisterBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.profilesetting.ProfileSettingActivity

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    val binding get() = requireNotNull(_binding)

    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModel.factory(
            analyticsHelper = AppModule.getInstance().analyticsHelper,
            kakaoLoginUseCase = AppModule.getInstance().kakaoLoginUseCase,
            getJwtTokenUseCase = AppModule.getInstance().getJwtTokenUseCase,
        )
    }

    private val googleSignInLauncher =
        registerForActivityResult(GoogleSignInContract()) { task ->
            val account =
                task?.getResult(ApiException::class.java) ?: return@registerForActivityResult
            val idToken = account.idToken ?: return@registerForActivityResult
            viewModel.handleGoogleLogin(idToken = idToken)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCreateView()
        splashScreen.setKeepOnScreenCondition {
            viewModel.splashLoading.value == true
        }
    }

    private fun initCreateView() {
        initDataBinding()
        initObserve()
    }

    private fun initDataBinding() {
        binding.apply {
            vm = viewModel
            lifecycleOwner = this@RegisterActivity
        }
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is RegisterNavigationAction.NavigateToAlreadyLogin -> {
                    startActivity(MainActivity.getIntent(this))
                    finish()
                }

                is RegisterNavigationAction.NavigateToGoogleLogin ->
                    googleSignInLauncher.launch(
                        SIGN_IN_REQUEST_CODE,
                    )

                is RegisterNavigationAction.NavigateToProfileSetting ->
                    startActivity(
                        ProfileSettingActivity.getIntent(this, null),
                    )
            }
        }
    }

    companion object {
        const val SIGN_IN_REQUEST_CODE = 1

        fun getIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        }
    }
}
