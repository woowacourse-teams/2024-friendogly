package com.happy.friendogly.presentation.ui.register

sealed interface RegisterNavigationAction {
    data object NavigateToAlreadyLogin : RegisterNavigationAction

    data object NavigateToGoogleLogin : RegisterNavigationAction

    data class NavigateToProfileSetting(val idToken: String) : RegisterNavigationAction
}
