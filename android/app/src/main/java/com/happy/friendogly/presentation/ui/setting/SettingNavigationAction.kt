package com.happy.friendogly.presentation.ui.setting

sealed interface SettingNavigationAction {
    data object NavigateToBack : SettingNavigationAction

    data object NavigateToAppInfo : SettingNavigationAction

    data object NavigateToPrivacyPolicy : SettingNavigationAction

    data object NavigateToLogout : SettingNavigationAction

    data object NavigateToUnsubscribe : SettingNavigationAction

    data object NavigateToRegister : SettingNavigationAction
}
