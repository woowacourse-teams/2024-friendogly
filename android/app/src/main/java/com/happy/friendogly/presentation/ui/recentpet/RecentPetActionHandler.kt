package com.happy.friendogly.presentation.ui.recentpet

interface RecentPetActionHandler {
    fun navigateToProfile(memberId: Long)

    fun navigateToPetImage(petImageUrl: String)
}
