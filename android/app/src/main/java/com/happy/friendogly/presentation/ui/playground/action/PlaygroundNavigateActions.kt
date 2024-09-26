package com.happy.friendogly.presentation.ui.playground.action

interface PlaygroundNavigateActions {
    data class NavigateToPetImage(val petImageUrl: String) : PlaygroundNavigateActions
}
