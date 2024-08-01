package com.happy.friendogly.presentation.ui.otherprofile

sealed interface OtherProfileNavigationAction {
    data object NavigateToBack : OtherProfileNavigationAction

    data class NavigateToDogDetail(val id: Long) : OtherProfileNavigationAction

    data class NavigateToUserMore(val id: Long) : OtherProfileNavigationAction
}
