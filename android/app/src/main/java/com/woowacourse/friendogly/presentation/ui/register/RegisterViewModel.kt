package com.woowacourse.friendogly.presentation.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit

class RegisterViewModel : BaseViewModel() {
    private val _navigateAction: MutableLiveData<Event<RegisterNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterNavigationAction>> get() = _navigateAction

    fun executeKakaoLogin() {
        _navigateAction.emit(RegisterNavigationAction.NavigateToKakaoLogin)
    }

    fun executeGoogleLogin() {
        _navigateAction.emit(RegisterNavigationAction.NavigateToGoogleLogin)
    }
}
