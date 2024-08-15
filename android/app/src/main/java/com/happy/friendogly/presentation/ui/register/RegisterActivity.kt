package com.happy.friendogly.presentation.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
            postKakaoLoginUseCase = AppModule.getInstance().postKakaoLoginUseCase,
            saveJwtTokenUseCase = AppModule.getInstance().saveJwtTokenUseCase,
        )
    }

    private var toast: Toast? = null

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
                    showToastMessage("현재 구글 로그인은 사용할 수 없어요")
//                    googleSignInLauncher.launch(SIGN_IN_REQUEST_CODE)
                }

                is RegisterNavigationAction.NavigateToProfileSetting ->
                    startActivity(
                        ProfileSettingActivity.getIntent(this, action.idToken, null),
                    )
            }
        }

        viewModel.message.observeEvent(this) { message ->
            // TODO 예시 코드로 메시지는 하드 코딩 했습니다. 서버에서 내려주는 에러 코드가 완성되면 수정하겠습니다.
            when (message) {
                RegisterMessage.DefaultErrorMessage -> showToastMessage("서버 에러가 발생했습니다")
                RegisterMessage.ServerErrorMessage -> showToastMessage("알 수 없는 에러가 발생했습니다")
            }
        }
    }

    private fun showToastMessage(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
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
