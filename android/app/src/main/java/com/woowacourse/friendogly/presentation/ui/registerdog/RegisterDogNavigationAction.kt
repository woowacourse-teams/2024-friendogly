package com.woowacourse.friendogly.presentation.ui.registerdog

sealed interface RegisterDogNavigationAction {
    data object NavigateToSetProfileImage : RegisterDogNavigationAction

    data class NavigateToSetBirthday(val year: Int, val month: Int) : RegisterDogNavigationAction

    data object NavigateToBack : RegisterDogNavigationAction

    data object NavigateToMyPage : RegisterDogNavigationAction
}
