package com.happy.friendogly.presentation.ui.mypage

import com.happy.friendogly.presentation.ui.petdetail.PetsDetail

sealed interface MyPageNavigationAction {
    data object NavigateToProfileEdit : MyPageNavigationAction

    data object NavigateToSetting : MyPageNavigationAction

    data object NavigateToDogRegister : MyPageNavigationAction

    data object NavigateToPetEdit : MyPageNavigationAction

    data class NavigateToPetDetail(val currentPage: Int, val petsDetail: PetsDetail) :
        MyPageNavigationAction

    data object NavigateToMyParticipation : MyPageNavigationAction

    data object NavigateToMyClubManger : MyPageNavigationAction
}
