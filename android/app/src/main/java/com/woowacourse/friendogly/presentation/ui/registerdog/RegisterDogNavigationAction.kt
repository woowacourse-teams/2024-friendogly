package com.woowacourse.friendogly.presentation.ui.registerdog

sealed interface RegisterDogNavigationAction {
    data object NavigateToSetProfileImage : RegisterDogNavigationAction

    data object NavigateToBack : RegisterDogNavigationAction

    data object NavigateToMyPage : RegisterDogNavigationAction
}
