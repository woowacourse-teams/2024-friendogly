package com.happy.friendogly.presentation.ui.mypage

sealed interface MyPageNavigationAction {
    data object NavigateToProfileEdit : MyPageNavigationAction

    data object NavigateToSetting : MyPageNavigationAction

    data object NavigateToDogRegister : MyPageNavigationAction

    data object NavigateToPetEdit : MyPageNavigationAction

    data class NavigateToDogDetail(val id: Long) : MyPageNavigationAction

    data object NavigateToMyParticipation : MyPageNavigationAction

    data object NavigateToMyClubManger : MyPageNavigationAction
}
