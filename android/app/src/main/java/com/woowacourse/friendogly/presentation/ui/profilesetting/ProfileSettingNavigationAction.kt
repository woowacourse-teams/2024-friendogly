package com.woowacourse.friendogly.presentation.ui.profilesetting

sealed interface ProfileSettingNavigationAction {
    data object NavigateToSetProfileImage : ProfileSettingNavigationAction

    data object NavigateToHome : ProfileSettingNavigationAction
}
