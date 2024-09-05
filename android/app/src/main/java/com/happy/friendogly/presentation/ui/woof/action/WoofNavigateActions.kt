package com.happy.friendogly.presentation.ui.woof.action

interface WoofNavigateActions {
    data class NavigateToPetImage(val petImageUrl: String) : WoofNavigateActions

    data class NavigateToOtherProfile(val memberId: Long) : WoofNavigateActions
}
