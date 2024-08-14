package com.happy.friendogly.presentation.ui.register

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.model.KakaoAccessToken
import com.happy.friendogly.domain.usecase.GetJwtTokenUseCase
import com.happy.friendogly.domain.usecase.KakaoLoginUseCase
import com.happy.friendogly.domain.usecase.PostKakaoLoginUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.utils.logGoogleLoginClicked
import com.happy.friendogly.presentation.utils.logKakaoLoginClicked
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val analyticsHelper: AnalyticsHelper,
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val getJwtTokenUseCase: GetJwtTokenUseCase,
    private val postKakaoLoginUseCase: PostKakaoLoginUseCase,
    private val saveJwtTokenUseCase: SaveJwtTokenUseCase,
) : BaseViewModel() {
    private val _navigateAction: MutableLiveData<Event<RegisterNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterNavigationAction>> get() = _navigateAction

    private val _message: MutableLiveData<Event<RegisterMessage>> =
        MutableLiveData(null)
    val message: LiveData<Event<RegisterMessage>> get() = _message

    val splashLoading = MutableLiveData(true)

    init {
        handleTokenState()
    }

    private fun handleTokenState() {
        viewModelScope.launch {
            getJwtTokenUseCase().onSuccess { jwtToken ->
                if (jwtToken?.accessToken.isNullOrBlank()) {
                    splashLoading.value = false
                    return@onSuccess
                }
                _navigateAction.emit(RegisterNavigationAction.NavigateToAlreadyLogin)
            }.onFailure {
                // TODO 예외처리
            }
        }
    }

    fun executeKakaoLogin(context: Context) {
        launch {
            kakaoLoginUseCase(context = context).onSuccess { kakaAccessToken ->
                kakaoLogin(kakaAccessToken)
            }.onFailure {
                // TODO 예외처리
            }
        }
    }

    private suspend fun kakaoLogin(kakaAccessToken: KakaoAccessToken) {
        val accessToken = kakaAccessToken.accessToken ?: return

        postKakaoLoginUseCase(accessToken = accessToken).fold(
            onSuccess = { login ->
                if (login.isRegistered) {
                    val tokens = login.tokens ?: return
                    saveAlarmToken()
                    saveJwtToken(tokens)
                } else {
                    _navigateAction.emit(RegisterNavigationAction.NavigateToProfileSetting(idToken = kakaAccessToken.accessToken))
                }
            },
            onError = { error ->
                when (error) {
                    DataError.Network.SERVER_ERROR -> _message.emit(RegisterMessage.ServerErrorMessage)
                    else -> _message.emit(RegisterMessage.DefaultErrorMessage)
                }
            },
        )
    }

    private fun saveAlarmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                val token = task.result
                launch {
                    runCatching {
                        AppModule.getInstance().saveAlarmTokenUseCase.invoke(token)
                    }.onFailure {
                        Log.d("테스트","$it")
                    }
                }
            }
    }

    fun executeGoogleLogin() {
        analyticsHelper.logGoogleLoginClicked()
        _navigateAction.emit(RegisterNavigationAction.NavigateToGoogleLogin)
    }

    fun handleGoogleLogin(idToken: String) {
        analyticsHelper.logKakaoLoginClicked()
        _navigateAction.emit(RegisterNavigationAction.NavigateToProfileSetting(idToken = idToken))
    }

    private suspend fun saveJwtToken(jwtToken: JwtToken) {
        saveJwtTokenUseCase(jwtToken = jwtToken).onSuccess {
            _navigateAction.emit(RegisterNavigationAction.NavigateToAlreadyLogin)
        }.onFailure {
            // TODO 예외처리
        }
    }

    companion object {
        fun factory(
            analyticsHelper: AnalyticsHelper,
            kakaoLoginUseCase: KakaoLoginUseCase,
            getJwtTokenUseCase: GetJwtTokenUseCase,
            postKakaoLoginUseCase: PostKakaoLoginUseCase,
            saveJwtTokenUseCase: SaveJwtTokenUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    kakaoLoginUseCase = kakaoLoginUseCase,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                )
            }
        }
    }
}
