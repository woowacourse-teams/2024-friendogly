package com.happy.friendogly.presentation.ui.profilesetting

sealed interface ProfileSettingNavigationAction {
    data object NavigateToSetProfileImage : ProfileSettingNavigationAction

    data object NavigateToHome : ProfileSettingNavigationAction

    data object NavigateToMyPage : ProfileSettingNavigationAction
}
