package com.woowacourse.friendogly.presentation.ui.group.add

sealed interface GroupAddEvent {
    sealed interface Navigation : GroupAddEvent {
        data object NavigateToHome : Navigation

        data object NavigateToSelectGroupPoster : Navigation

        data object NavigateToSelectDog : Navigation
    }

    data class ChangePage(val page: Int) : GroupAddEvent
}
