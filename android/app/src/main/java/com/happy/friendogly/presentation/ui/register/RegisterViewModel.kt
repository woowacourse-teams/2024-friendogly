package com.happy.friendogly.presentation.ui.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.domain.usecase.GetJwtTokenUseCase
import com.happy.friendogly.domain.usecase.KakaoLoginUseCase
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
) : BaseViewModel() {
    private val _navigateAction: MutableLiveData<Event<RegisterNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterNavigationAction>> get() = _navigateAction

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
        viewModelScope.launch {
            kakaoLoginUseCase(context = context).onSuccess { kakaAccessToken ->
                kakaAccessToken.idToken ?: return@onSuccess
                _navigateAction.emit(RegisterNavigationAction.NavigateToProfileSetting(idToken = kakaAccessToken.idToken))
            }.onFailure { }
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

    companion object {
        fun factory(
            analyticsHelper: AnalyticsHelper,
            kakaoLoginUseCase: KakaoLoginUseCase,
            getJwtTokenUseCase: GetJwtTokenUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                RegisterViewModel(
                    analyticsHelper = analyticsHelper,
                    kakaoLoginUseCase = kakaoLoginUseCase,
                    getJwtTokenUseCase = getJwtTokenUseCase,
                )
            }
        }
    }
}
