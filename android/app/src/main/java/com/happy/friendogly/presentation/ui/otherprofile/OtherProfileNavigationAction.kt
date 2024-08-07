package com.happy.friendogly.presentation.ui.otherprofile

import com.happy.friendogly.presentation.ui.petdetail.PetsDetail

sealed interface OtherProfileNavigationAction {
    data object NavigateToBack : OtherProfileNavigationAction

    data class NavigateToPetDetail(
        val currentPage: Int,
        val petsDetail: PetsDetail,
    ) : OtherProfileNavigationAction

    data class NavigateToUserMore(val id: Long) : OtherProfileNavigationAction
}
