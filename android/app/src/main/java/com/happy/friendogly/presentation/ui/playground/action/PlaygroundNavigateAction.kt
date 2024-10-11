package com.happy.friendogly.presentation.ui.playground.action

interface PlaygroundNavigateAction {
    data class NavigateToOtherProfile(val memberId: Long) : PlaygroundNavigateAction

    data class NavigateToPetImage(val petImageUrl: String) : PlaygroundNavigateAction
}
