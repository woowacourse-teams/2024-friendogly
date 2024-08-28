package com.happy.friendogly.presentation.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.model.KakaoAccessToken
import com.happy.friendogly.domain.usecase.GetJwtTokenUseCase
import com.happy.friendogly.domain.usecase.PostKakaoLoginUseCase
import com.happy.friendogly.domain.usecase.SaveAlamTokenUseCase
import com.happy.friendogly.domain.usecase.SaveJwtTokenUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.utils.logGoogleLoginClicked
import com.happy.friendogly.presentation.utils.logKakaoLoginClicked

class RegisterViewModel(
    private val analyticsHelper: AnalyticsHelper,
    private val getJwtTokenUseCase: GetJwtTokenUseCase,
    private val postKakaoLoginUseCase: PostKakaoLoginUseCase,
    private val saveJwtTokenUseCase: SaveJwtTokenUseCase,
    private val saveAlarmTokenUseCase: SaveAlamTokenUseCase,
) : BaseViewModel() {
    private val _navigateAction: MutableLiveData<Event<RegisterNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterNavigationAction>> get() = _navigateAction

    private val _message: MutableLiveData<Event<RegisterMessage>> =
        MutableLiveData(null)
    val message: LiveData<Event<RegisterMessage>> get() = _message

    val splashLoading = MutableLiveData(true)

    private val _loading: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val loading: LiveData<Event<Boolean>> get() = _loading

    init {
        handleTokenState()
    }

    private fun handleTokenState() {
        launch {
            getJwtTokenUseCase().fold(
                onSuccess = { jwtToken ->
                    if (jwtToken?.accessToken.isNullOrBlank()) {
                        splashLoading.value = false
                        return@launch
                    }
                    _navigateAction.emit(RegisterNavigationAction.NavigateToAlreadyLogin)
                },
                onError = { error ->
                    when (error) {
                        DataError.Local.TOKEN_NOT_STORED -> _message.emit(RegisterMessage.TokenNotStoredErrorMessage)
                        else -> _message.emit(RegisterMessage.DefaultErrorMessage)
                    }
                },
            )
        }
    }

    fun executeKakaoLogin() {
        _navigateAction.emit(RegisterNavigationAction.NavigateToKakaoLogin)
    }

    fun postKakaoLogin(kakaAccessToken: KakaoAccessToken) {
        launch {
            val accessToken = kakaAccessToken.accessToken ?: return@launch

            _loading.emit(true)
            postKakaoLoginUseCase(accessToken = accessToken).fold(
                onSuccess = { login ->
                    if (login.isRegistered) {
                        val tokens = login.tokens ?: return@fold
                        saveAlarmToken()
                        saveJwtToken(tokens)
                    } else {
                        _navigateAction.emit(
                            RegisterNavigationAction.NavigateToProfileSetting(
                                idToken = kakaAccessToken.accessToken,
                            ),
                        )
                    }
                },
                onError = { error ->
                    when (error) {
                        DataError.Network.NO_INTERNET -> _message.emit(RegisterMessage.NoInternetMessage)
                        DataError.Network.SERVER_ERROR -> _message.emit(RegisterMessage.ServerErrorMessage)
                        else -> _message.emit(RegisterMessage.DefaultErrorMessage)
                    }
                },
            )
            _loading.emit(false)
        }
    }

    private fun saveAlarmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                val token = task.result
                launch { // TODO 에러 핸들링
                    AppModule.getInstance().saveAlarmTokenUseCase.invoke(token)
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
        saveJwtTokenUseCase(jwtToken = jwtToken).fold(
            onSuccess = {
                _navigateAction.emit(RegisterNavigationAction.NavigateToAlreadyLogin)
            },
            onError = { error ->
                when (error) {
                    DataError.Local.TOKEN_NOT_STORED -> _message.emit(RegisterMessage.TokenNotStoredErrorMessage)
                    else -> _message.emit(RegisterMessage.DefaultErrorMessage)
                }
            },
        )
    }

    companion object {
        fun factory(
            analyticsHelper: AnalyticsHelper,
            getJwtTokenUseCase: GetJwtTokenUseCase,
            postKakaoLoginUseCase: PostKakaoLoginUseCase,
            saveJwtTokenUseCase: SaveJwtTokenUseCase,
            saveAlarmTokenUseCase: SaveAlamTokenUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                    postKakaoLoginUseCase = postKakaoLoginUseCase,
                    saveJwtTokenUseCase = saveJwtTokenUseCase,
                    saveAlarmTokenUseCase = saveAlarmTokenUseCase,
                )
            }
        }
    }
}
