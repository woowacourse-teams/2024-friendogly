package com.happy.friendogly.presentation.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ApiException
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivityRegisterBinding
import com.happy.friendogly.domain.fold
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.LoadingDialog
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.profilesetting.ProfileSettingActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    val binding get() = requireNotNull(_binding)

    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModel.factory(
            analyticsHelper = AppModule.getInstance().analyticsHelper,
            getJwtTokenUseCase = AppModule.getInstance().getJwtTokenUseCase,
            postKakaoLoginUseCase = AppModule.getInstance().postKakaoLoginUseCase,
            saveJwtTokenUseCase = AppModule.getInstance().saveJwtTokenUseCase,
            saveAlarmTokenUseCase = AppModule.getInstance().saveAlarmTokenUseCase,
            getFCMTokenUseCase = AppModule.getInstance().getFCMTokenUseCase,
        )
    }
    private val kakaoLoginUseCase = AppModule.getInstance().kakaoLoginUseCase

    private var toast: Toast? = null

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

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

                is RegisterNavigationAction.NavigateToGoogleLogin -> {
                    // TODO 구글 로그인 x
                    googleSignInLauncher.launch(SIGN_IN_REQUEST_CODE)
                }

                is RegisterNavigationAction.NavigateToKakaoLogin -> kakaoLogin()

                is RegisterNavigationAction.NavigateToProfileSetting ->
                    startActivity(
                        ProfileSettingActivity.getIntent(this, action.idToken, null),
                    )
            }
        }

        viewModel.loading.observeEvent(this) { loading ->
            if (loading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        }

        viewModel.message.observeEvent(this) { message ->
            when (message) {
                is RegisterMessage.DefaultErrorMessage -> showToastMessage(getString(R.string.server_error_message))
                is RegisterMessage.ServerErrorMessage -> showToastMessage(getString(R.string.default_error_message))
                is RegisterMessage.TokenNotStoredErrorMessage ->
                    startActivity(MainActivity.getIntent(this))

                is RegisterMessage.NoInternetMessage -> showToastMessage(getString(R.string.no_internet_message))
            }
        }
    }

    private fun kakaoLogin() {
        lifecycleScope.launch {
            kakaoLoginUseCase(context = this@RegisterActivity).fold(
                onSuccess = { kakaAccessToken ->
                    viewModel.postKakaoLogin(kakaAccessToken)
                },
                onError = {
                    showToastMessage(getString(R.string.kakao_login_error_message))
                },
            )
        }
    }

    private fun showToastMessage(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun showLoadingDialog() {
        loadingDialog.show()
    }

    private fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
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
