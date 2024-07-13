package com.woowacourse.friendogly.presentation.ui.register

sealed interface RegisterNavigationAction {
    data object NavigateToKakaoLogin : RegisterNavigationAction

    data object NavigateToGoogleLogin : RegisterNavigationAction

    data class NavigateToProfileSetting(val idToken: String) : RegisterNavigationAction
}
