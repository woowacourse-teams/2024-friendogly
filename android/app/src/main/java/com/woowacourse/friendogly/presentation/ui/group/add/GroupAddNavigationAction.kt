package com.woowacourse.friendogly.presentation.ui.group.add

sealed interface GroupAddNavigationAction {
    data object NavigateToHome: GroupAddNavigationAction
    data object NavigateToSelectGroupPoster: GroupAddNavigationAction
    data object NavigateToSelectDog: GroupAddNavigationAction
}
