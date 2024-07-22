package com.woowacourse.friendogly.presentation.ui.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.domain.usecase.KakaoLoginUseCase
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
) : BaseViewModel() {
    private val _navigateAction: MutableLiveData<Event<RegisterNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterNavigationAction>> get() = _navigateAction

    fun executeKakaoLogin(context: Context) {
        viewModelScope.launch {
            kakaoLoginUseCase(context = context).onSuccess { kakaAccessToken ->
                kakaAccessToken.idToken ?: return@onSuccess
                _navigateAction.emit(RegisterNavigationAction.NavigateToProfileSetting(idToken = kakaAccessToken.idToken))
            }.onFailure { }
        }
    }

    fun executeGoogleLogin() {
        _navigateAction.emit(RegisterNavigationAction.NavigateToGoogleLogin)
    }

    fun handleGoogleLogin(idToken: String) {
        _navigateAction.emit(RegisterNavigationAction.NavigateToProfileSetting(idToken = idToken))
    }

    companion object {
        fun factory(kakaoLoginUseCase: KakaoLoginUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                RegisterViewModel(kakaoLoginUseCase = kakaoLoginUseCase)
            }
        }
    }
}
