package com.woowacourse.friendogly.presentation.ui.mypage

sealed interface MyPageNavigationAction {
    data object NavigateToProfileEdit : MyPageNavigationAction

    data object NavigateToSetting : MyPageNavigationAction

    data object NavigateToDogRegister : MyPageNavigationAction

    data object NavigateToDogDetail : MyPageNavigationAction
}
