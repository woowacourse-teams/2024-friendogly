package com.woowacourse.friendogly.presentation.ui.group.add

sealed interface GroupAddNavigationAction {
    data object NavigateToHome: GroupAddNavigationAction
    data object NavigateToGroupPoster: GroupAddNavigationAction
    data object NavigateToDogSelector: GroupAddNavigationAction
}
