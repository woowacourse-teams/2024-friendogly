package com.happy.friendogly.presentation.ui.register

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.common.api.ApiException
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivityRegisterBinding
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.profilesetting.ProfileSettingActivity
import com.kakao.sdk.common.util.Utility

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    val binding get() = requireNotNull(_binding)

    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModel.factory(
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
        var keyHash = Utility.getKeyHash(this)
        Log.d( "keyhash :", "$keyHash")
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
                        ProfileSettingActivity.getIntent(this),
                    )
            }
        }
    }

    companion object {
        const val SIGN_IN_REQUEST_CODE = 1
    }
}
