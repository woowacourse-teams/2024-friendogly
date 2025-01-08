package com.happy.friendogly.presentation.ui.recentpet

sealed interface RecentPetNavigationAction {
    data object NavigateToBack : RecentPetNavigationAction

    data class NavigateToOtherProfile(val memberId: Long) : RecentPetNavigationAction

    data class NavigateToPetImage(val petImageUrl: String) : RecentPetNavigationAction
}
